package com.rafaeldsal.ws.minhaprata.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentWebhookResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.KafkaSubscribeException;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
import com.rafaeldsal.ws.minhaprata.utils.DateTimeUtils;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentStatusNotifierConsumerTest {

  private final String TRANSACTION_ID = IdGenerator.UUIDGenerator("idempotence");
  private final String PAYMENT_INTENT_ID = "pi_3RMBfkC8jyET0Scr04qpUJbY";
  private final String STATUS = "succeeded";
  private final String VALID_MESSAGE = """
    {
        "transactionId": "%s",
        "status": "%s",
        "timestamp": "%s",
        "paymentIntentId": "%s",
        "paymentErrorInfo": null
    }
    """.formatted(TRANSACTION_ID, STATUS, DateTimeUtils.timestamp(), PAYMENT_INTENT_ID);
  private final String INVALID_MESSAGE = "invalid-message";

  @Mock
  private ObjectMapper objectMapper;
  @Mock
  private PaymentService paymentService;
  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @InjectMocks
  private PaymentStatusNotifierConsumer consumer;

  @Test
  void given_consumerPaymentIntentStatusUpdatedByWebhook_when_sendMessageSuccessfully_then_sendMessage() throws Exception {
    PaymentWebhookResponseDto dto = PaymentWebhookResponseDto.builder()
        .transactionId(TRANSACTION_ID)
        .paymentIntentId(PAYMENT_INTENT_ID)
        .status(STATUS)
        .paymentErrorInfo(null)
        .timestamp(DateTimeUtils.timestamp())
        .build();

    when(objectMapper.readValue(VALID_MESSAGE, PaymentWebhookResponseDto.class)).thenReturn(dto);

    consumer.consumerPaymentIntentStatusUpdatedByWebhook(VALID_MESSAGE);

    verify(paymentService).updateStatusPaymentCompleted(dto);
  }

  @Test
  void given_consumerPaymentIntentStatusUpdatedByWebhook_when_messageIsInvalid_then_throwsJsonProcessingException() throws Exception {
    when(objectMapper.readValue(INVALID_MESSAGE, PaymentWebhookResponseDto.class))
        .thenThrow(new JsonProcessingException("Invalid JSON") {});

    KafkaSubscribeException exception = assertThrows(KafkaSubscribeException.class, () -> {
      consumer.consumerPaymentIntentStatusUpdatedByWebhook(INVALID_MESSAGE);
    });

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
    assertTrue(exception.getMessage().contains("Falha ao deserializar PaymentRequest"));
  }
}