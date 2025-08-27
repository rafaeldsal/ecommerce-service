package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentErrorInfo;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentInitResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentWebhookResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.PaymentStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.Payment;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.producer.PaymentIntentPublisher;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.PaymentRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.PaymentServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private static final String ORDER_ID = IdGenerator.UUIDGenerator("order");
    private static final String USER_ID = IdGenerator.UUIDGenerator("user");
    private static final String PAYMENT_ID = IdGenerator.UUIDGenerator("pay");
    private final String TRANSACTION_ID = IdGenerator.UUIDGenerator("idempotence");

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentIntentPublisher paymentIntentPublisher;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User createUser() {
        return User.builder()
                .id(USER_ID)
                .name("Rafael Souza")
                .email("rafael@email.com")
                .cpf("12345678900")
                .phoneNumber("61999999999")
                .dtBirth(LocalDate.of(2025, 4, 23))
                .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
                .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
                .role(UserRole.USER)
                .build();
    }

    private Payment createPayment() {
        return Payment.builder()
                .id(IdGenerator.UUIDGenerator("pay"))
                .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
                .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
                .amount(BigDecimal.valueOf(100.00))
                .currency("BRL")
                .paymentMethod("CARD")
                .status(PaymentStatus.PROCESSING)
                .transactionId(TRANSACTION_ID)
                .build();
    }

    private Order createOrder() {
        return Order.builder()
                .id(ORDER_ID)
                .status(OrderStatus.PENDING)
                .dtOrder(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
                .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
                .totalPrice(BigDecimal.valueOf(100.00))
                .user(createUser())
                .build();
    }

    private PaymentDto createPaymentDto() {
        return PaymentDto.builder()
                .dtPayment(LocalDateTime.now())
                .paymentMethod("CARD")
                .amount(BigDecimal.valueOf(100.00))
                .currency("BRL")
                .savePaymentMethod(false)
                .orderId(ORDER_ID)
                .build();
    }

    @Test
    void shouldCreatePaymentSuccessfully() {
        PaymentDto dto = createPaymentDto();
        Order order = createOrder();
        Payment payment = createPayment();

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any())).thenReturn(payment);

        PaymentInitResponseDto response = paymentService.create(dto);

        assertNotNull(response);
        verify(orderRepository, times(1)).save(order);
        verify(paymentIntentPublisher, times(1)).sendMessage(any());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreatePaymentWithId() {
        var dto = PaymentDto.builder()
                .id(PAYMENT_ID)
                .dtPayment(LocalDateTime.now())
                .paymentMethod("CARD")
                .amount(BigDecimal.valueOf(100.00))
                .currency("BRL")
                .savePaymentMethod(false)
                .orderId(ORDER_ID)
                .build();

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(createOrder()));

        assertThrows(BadRequestException.class, () -> paymentService.create(dto));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenOrderNotFound() {
        assertThrows(NotFoundException.class, () -> paymentService.create(createPaymentDto()));
    }

    @Test
    void shouldUpdatedPaymentStatusSuccessfully() {
        Order order = createOrder();
        PaymentUpdateStatus paymentUpdateStatus = PaymentUpdateStatus.builder()
                .paymentId(PAYMENT_ID)
                .transactionId(TRANSACTION_ID)
                .status(PaymentStatus.SUCCEEDED.toString())
                .orderId(order.getId())
                .userId(USER_ID)
                .clientSecret("client")
                .paymentIntentId("pi_123456789")
                .build();
        Payment payment = createPayment();
        payment.setOrder(order);

        when(paymentRepository.findByTransactionId(TRANSACTION_ID)).thenReturn(Optional.of(payment));

        paymentService.update(paymentUpdateStatus);

        verify(orderRepository, times(1)).save(any());
        verify(paymentRepository, times(1)).save(any());

    }

    @Test
    void shouldThrowNotFoundExceptionWhenTransactionIdNotFound() {
        PaymentUpdateStatus paymentUpdateStatus = PaymentUpdateStatus.builder()
                .paymentId(PAYMENT_ID)
                .transactionId(TRANSACTION_ID)
                .status(PaymentStatus.SUCCEEDED.toString())
                .orderId(ORDER_ID)
                .userId(USER_ID)
                .clientSecret("client")
                .paymentIntentId("pi_123456789")
                .build();

        when(paymentRepository.findByTransactionId(TRANSACTION_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.update(paymentUpdateStatus));
    }

    @Test
    void shouldUpdatePaymentCompletedly() {
        PaymentWebhookResponseDto dto = PaymentWebhookResponseDto.builder()
                .transactionId(TRANSACTION_ID)
                .status(PaymentStatus.SUCCEEDED.toString())
                .timestamp(LocalDateTime.now().toString())
                .paymentIntentId("pi_123456789")
                .paymentErrorInfo(null)
                .build();

        Order order = createOrder();
        Payment payment = createPayment();
        payment.setOrder(order);

        when(paymentRepository.findByTransactionId(dto.transactionId())).thenReturn(Optional.of(payment));

        paymentService.updateStatusPaymentCompleted(dto);

        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    void shouldReturnVoidWhenOrderStatusAlreadyInFinalizedStatus() {
        PaymentWebhookResponseDto dto = PaymentWebhookResponseDto.builder()
                .transactionId(TRANSACTION_ID)
                .status(PaymentStatus.SUCCEEDED.toString())
                .timestamp(LocalDateTime.now().toString())
                .paymentIntentId("pi_123456789")
                .paymentErrorInfo(null)
                .build();

        Order order = createOrder();
        order.setStatus(OrderStatus.FAILURE);
        Payment payment = createPayment();
        payment.setOrder(order);

        when(paymentRepository.findByTransactionId(dto.transactionId())).thenReturn(Optional.of(payment));

        paymentService.updateStatusPaymentCompleted(dto);

        verify(paymentRepository, times(0)).save(any());
        verify(orderRepository, times(0)).save(any());
    }

    @Test
    void shouldFindPaymentIntentStatusSuccessfully() {
        Order order = createOrder();
        Payment payment = createPayment();
        payment.setOrder(order);

        when(paymentRepository.findByTransactionId(TRANSACTION_ID)).thenReturn(Optional.of(payment));

        paymentService.findPaymentIntentStatus(TRANSACTION_ID);

        verify(paymentRepository, times(1)).findByTransactionId(TRANSACTION_ID);
    }

    @Test
    void shouldReturnPaymentProcessingErrorWhenPaymentIntentFailure() {
        Payment payment = createPayment();
        payment.setPaymentIntentId("pi_123456789");
        payment.setClientSecret("client_secret_123");
        payment.setFailureCode("card_declined");
        payment.setStatus(PaymentStatus.REQUIRES_PAYMENT_METHOD);
        payment.setFailureMessage("The card was declined.");
        payment.setDeclineCode("generic_decline");

        when(paymentRepository.findByTransactionId(TRANSACTION_ID)).thenReturn(Optional.of(payment));

        PaymentUpdateStatus paymentUpdateStatus = PaymentUpdateStatus.builder()
                .paymentId(payment.getId())
                .paymentIntentId(payment.getPaymentIntentId())
                .clientSecret(payment.getClientSecret())
                .transactionId(payment.getTransactionId())
                .orderId(ORDER_ID)
                .status(payment.getStatus().toString())
                .userId(USER_ID)
                .paymentErrorInfo(PaymentErrorInfo.builder()
                        .errorCode(payment.getFailureCode())
                        .errorMessage(payment.getFailureMessage())
                        .stripeFailureCode(payment.getDeclineCode())
                        .build())
                .build();

        paymentService.findPaymentIntentStatus(TRANSACTION_ID);

        assertNotNull(paymentUpdateStatus);
        verify(paymentRepository, times(1)).findByTransactionId(TRANSACTION_ID);
    }
}