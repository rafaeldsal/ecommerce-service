package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.order.OrderMapper;
import com.rafaeldsal.ws.minhaprata.mapper.payment.PaymentMapper;
import com.rafaeldsal.ws.minhaprata.mapper.user.UserPaymentInfoMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Payment;
import com.rafaeldsal.ws.minhaprata.producer.PaymentEventProducer;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.PaymentRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserPaymentInfoRepository;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;
  private final UserPaymentInfoRepository userPaymentInfoRepository;
  private final PaymentEventProducer paymentEventProducer;

  // Mudar o retorno desse método
  @Override
  public Payment create(PaymentDto dto) {
    var order = orderRepository.findById(dto.orderId())
        .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

    if (dto.id() != null) {
      throw new BadRequestException("Campo ID deve ser nulo");
    }

    // Criar pagamento no banco
    var payment = PaymentMapper.toEntity(dto, order);
    paymentRepository.save(payment);

    // Se savePaymentMethod == true, salvar os dados do cartão, encriptografado
    if (dto.savePaymentMethod() && dto.paymentMethod().equalsIgnoreCase("card")) {
      try {
        var userPaymentInfo = UserPaymentInfoMapper.toEntity(dto, order.getUser());
        userPaymentInfoRepository.save(userPaymentInfo);
      } catch (Exception e) {
        log.warn("Não foi possível salvar os dados de pagamento do usuário: {}", e.getMessage());
      }
    }

    // Enviar informações ao Kafka
    // Preparar evento - PaymentDtoRequest -> OrderEvent -> InfoCard
    try {
      String requestId = IdGenerator.UUIDGenerator("idempotence");

      var orderEvent = OrderMapper.toOrderEventDto(order);
      var infoCard = PaymentMapper.toSensitiveInfoCard(dto);
      var paymentEvent = PaymentMapper.toEventDto(requestId, orderEvent, infoCard, dto);

      paymentEventProducer.sendMessage(paymentEvent);
      return payment;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
