package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentInitResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentWebhookResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.payment.PaymentMapper;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.PaymentStatus;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.Payment;
import com.rafaeldsal.ws.minhaprata.producer.PaymentIntentPublisher;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.PaymentRepository;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;
  private final PaymentIntentPublisher paymentIntentPublisher;
  private final OrderService orderService;

  @Override
  @Transactional
  public PaymentInitResponseDto create(PaymentDto dto) {
    var order = validateAndGetOrder(dto);
    validatePaymentId(dto.id());

    String paymentId = IdGenerator.UUIDGenerator("pay");
    String transactionId = IdGenerator.UUIDGenerator("idempotence");

    var payment = createAndSavePaymentEntity(dto, order, paymentId, transactionId);

    order.setPayment(payment);
    orderRepository.save(order);

    publishPaymentEvent(order, payment);

    return PaymentMapper.toResponseDto(payment);
  }

  @Override
  @Transactional
  public void update(PaymentUpdateStatus dto) {
    log.info("Atualizando informações de pagamento. Id de transação: {}", dto.paymentIntentId(), dto.transactionId());
    var payment = paymentRepository.findByTransactionId(dto.transactionId())
        .orElseThrow(() -> new NotFoundException("Não foi encontrado pagamento com esse ID " + dto.transactionId()));

    Order order = payment.getOrder();
    PaymentMapper.updatedToEntity(dto, payment);

    order.setPayment(payment);
    orderRepository.save(order);
    paymentRepository.save(payment);

    log.info("Informações do pagamento '{}' atualizado com sucesso", payment.getId());
  }

  @Override
  @Transactional
  public void updateStatusPaymentCompleted(PaymentWebhookResponseDto dto) {
    log.info("Atualizando status do pagamento após processamento do WebHook - ID de transação: {} - ID da Intenção de Pagamento: {} - Status do Pagamento: {}",
        dto.transactionId(),
        dto.paymentIntentId(),
        dto.status());

    var payment = getPaymentByTransactionId(dto.transactionId());

    PaymentStatus paymentStatus = switch (dto.status().toLowerCase()) {
      case "succeeded" -> PaymentStatus.SUCCEEDED;
      case "processing" -> PaymentStatus.PROCESSING;
      case "canceled" -> PaymentStatus.CANCELED;
      case "requires_payment_method", "requires_action", "requires_confirmation" ->
          shouldFailPermanently(dto.paymentErrorInfo().errorCode()) ? PaymentStatus.FAILED : PaymentStatus.REQUIRES_PAYMENT_METHOD;
      default -> PaymentStatus.FAILED;
    };

    PaymentMapper.updatedToEntityFinally(dto, payment, paymentStatus);

    OrderStatus statusOrder = switch (dto.status().toLowerCase()) {
      case "succeeded" -> OrderStatus.PAID;
      case "processing" -> OrderStatus.IN_PROCESSING;
      case "requires_payment_method", "requires_action", "requires_confirmation" ->
          shouldFailPermanently(dto.paymentErrorInfo().errorCode()) ? OrderStatus.FAILURE : OrderStatus.PENDING_PAYMENT;
      case "cancelled" -> OrderStatus.CANCELLED;
      default -> OrderStatus.FAILURE;
    };

    Order order = payment.getOrder();

    if (order.getStatus().isFinalStatus()) {
      log.warn("Pedido '{}' já está em status final: {}", order.getId(), order.getStatus());
      return;
    }

    order.setPayment(payment);
    orderService.updateStatusFromWebhook(order, statusOrder);

    paymentRepository.save(payment);
  }

  @Override
  public PaymentUpdateStatus findPaymentIntentStatus(String transactionId) {
    log.info("Buscando informações sobre a intenção de pagamento");
    var payment = getPaymentByTransactionId(transactionId);

    if (PaymentStatus.REQUIRES_PAYMENT_METHOD.equals(payment.getStatus()) &&
        payment.getFailureCode() != null) {
      log.warn("Pagamento falhou: {} - {}", payment.getFailureCode(), payment.getFailureMessage());
      return PaymentMapper.paymentProcessingError(payment);
    }

    return PaymentMapper.paymentProcessed(payment);
  }

  private boolean shouldFailPermanently(String errorCode) {
    if (errorCode == null) return false;

    return switch (errorCode) {
      case "expired_card", "incorrect_cvc", "incorrect_zip", "fraudulent",
           "lost_card", "stolen_card" -> true;
      default -> false;
    };
  }

  private void publishPaymentEvent(Order order, Payment payment) {
    paymentIntentPublisher.sendMessage(PaymentMapper.toEventDto(order, payment));
  }

  private Payment createAndSavePaymentEntity(PaymentDto dto, Order order, String paymentId, String transactionId) {
    var payment = PaymentMapper.toEntity(dto, order, paymentId, transactionId);
    paymentRepository.save(payment);
    return payment;
  }

  private static void validatePaymentId(String id) {
    if (id != null) {
      throw new BadRequestException("Campo ID deve ser nulo");
    }
  }

  private Order validateAndGetOrder(PaymentDto dto) {
    return orderRepository.findById(dto.orderId())
        .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
  }

  private Payment getPaymentByTransactionId(String transactionId) {
    return paymentRepository.findByTransactionId(transactionId)
        .orElseThrow(() -> new NotFoundException("Pagamento não encontrado"));
  }
}
