package com.rafaeldsal.ws.minhaprata.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.exception.KafkaSubscribeException;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentStatusUpdateConsumerTest {

  private final String TRANSACTION_ID = IdGenerator.UUIDGenerator("idempotence");
  private final String PAYMENT_ID = IdGenerator.UUIDGenerator("pay");
  private final String PAYMENT_INTENT_ID = "pi_3RMBfkC8jyET0Scr04qpUJbY";
  private final String CLIENT_SECRET = "client_secret_stripe";
  private final String ORDER_ID = IdGenerator.UUIDGenerator("order");
  private final String USER_ID = IdGenerator.UUIDGenerator("user");
  private final String STATUS = "succeeded";

  private final String VALID_MESSAGE = """
      {
        "paymentId": "%s",
        "transactionId": "%s",
        "status": "%s",
        "orderId": "%s",
        "userId": "%s",
        "clientSecret": "%s",
        "paymentIntentId": "%s",
        "paymentErrorInfo": null
      }
      """.formatted(PAYMENT_ID, TRANSACTION_ID, STATUS, ORDER_ID, USER_ID, CLIENT_SECRET, PAYMENT_INTENT_ID);
  private final String INVALID_MESSAGE = "invalid_message";

  @Mock
  private ObjectMapper objectMapper;
  @Mock
  private PaymentService paymentService;
  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @InjectMocks
  private PaymentStatusUpdateConsumer consumer;

  @Test
  void given_consumerPaymentIntentStatusUpdate_when_receiveMessageSuccessfully_then_receivesMessage() throws Exception {
    PaymentUpdateStatus dto = PaymentUpdateStatus.builder()
        .status(STATUS)
        .paymentIntentId(PAYMENT_INTENT_ID)
        .paymentId(PAYMENT_ID)
        .clientSecret(CLIENT_SECRET)
        .orderId(ORDER_ID)
        .transactionId(TRANSACTION_ID)
        .userId(USER_ID)
        .paymentErrorInfo(null)
        .build();

    when(objectMapper.readValue(VALID_MESSAGE, PaymentUpdateStatus.class)).thenReturn(dto);

    consumer.consumerPaymentIntentStatusUpdate(VALID_MESSAGE);

    verify(paymentService, times(1)).update(dto);
  }

  @Test
  void given_consumerPaymentIntentStatusUpdate_when_messageIsInvalid_then_throwsJsonProcessingException() throws Exception {
    when(objectMapper.readValue(INVALID_MESSAGE, PaymentUpdateStatus.class))
        .thenThrow(new JsonProcessingException("Invalid JSON") {});

    KafkaSubscribeException exception = assertThrows(KafkaSubscribeException.class, () -> {
      consumer.consumerPaymentIntentStatusUpdate(INVALID_MESSAGE);
    });

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
    assertTrue(exception.getMessage().contains("Falha ao deserializar PaymentRequest"));
  }
}