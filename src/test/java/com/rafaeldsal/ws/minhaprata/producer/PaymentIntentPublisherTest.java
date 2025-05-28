package com.rafaeldsal.ws.minhaprata.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentRecord;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentWebhookResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.KafkaPublisherException;
import com.rafaeldsal.ws.minhaprata.producer.impl.PaymentIntentPublisherImpl;
import com.rafaeldsal.ws.minhaprata.utils.DateTimeUtils;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.record.RecordMetaData;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentIntentPublisherTest {

  @Mock
  private Executor callbackExecutor;
  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;
  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private PaymentIntentPublisherImpl paymentIntentPublisher;

  private final String TOPIC = "topic-test";
  private final String TRANSACTION_ID = IdGenerator.UUIDGenerator("idempotence");
  private final PaymentRecord PAYMENT_RECORD = PaymentRecord.builder()
      .transactionId(TRANSACTION_ID)
      .amount(1000L)
      .currency("BRL")
      .orderId(IdGenerator.UUIDGenerator("order"))
      .paymentMethod("card")
      .userId(IdGenerator.UUIDGenerator("user"))
      .timestamp(DateTimeUtils.timestamp())
      .build();
  private final String MESSAGE = """
      {
        "transactionId": "%s",
        "timestamp": "%s",
        "userId": "%s",
        "orderId": "%s",
        "paymentMethod": "card",
        "currency": "BRL",
        "amount": %d
      }
      """.formatted(
      TRANSACTION_ID,
      DateTimeUtils.timestamp(),
      IdGenerator.UUIDGenerator("user"),
      IdGenerator.UUIDGenerator("order"),
      1000L);

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(paymentIntentPublisher, "topicPaymentCreated", TOPIC);
  }

  @Test
  void given_sendMessage_when_serializeAndSendMessageSuccessfully_then_logMessageSuccess() throws Exception {
    when(objectMapper.writeValueAsString(PAYMENT_RECORD)).thenReturn(MESSAGE);

    CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
    RecordMetadata metadata = new RecordMetadata(new TopicPartition(TOPIC, 0), 0, 0, 0, 0L, 0,0);
    SendResult<String, String> sendResult = new SendResult<>(null, metadata);
    future.complete(sendResult);

    when(kafkaTemplate.send(TOPIC, TRANSACTION_ID, MESSAGE)).thenReturn(future);

    paymentIntentPublisher.sendMessage(PAYMENT_RECORD);

    verify(objectMapper, times(1)).writeValueAsString(PAYMENT_RECORD);
    verify(kafkaTemplate, times(1)).send(TOPIC, TRANSACTION_ID, MESSAGE);
  }

  @Test
  void given_sendMessage_when_sendFails_then_logError() throws Exception{
    String jsonContent = "{}";
    when(objectMapper.writeValueAsString(PAYMENT_RECORD)).thenReturn(jsonContent);

    CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException("Kafka error"));

    when(kafkaTemplate.send(TOPIC, TRANSACTION_ID, jsonContent)).thenReturn(future);

    paymentIntentPublisher.sendMessage(PAYMENT_RECORD);

    verify(objectMapper, times(1)).writeValueAsString(PAYMENT_RECORD);
    verify(kafkaTemplate, times(1)).send(TOPIC, TRANSACTION_ID, jsonContent);
  }

  @Test
  void given_sendMessage_when_serializeFails_then_throwsJsonProcessingException() throws Exception {

    when(objectMapper.writeValueAsString(PAYMENT_RECORD))
        .thenThrow(new JsonProcessingException("Invalid JSON") {});

    KafkaPublisherException exception = assertThrows(KafkaPublisherException.class, () -> {
      paymentIntentPublisher.sendMessage(PAYMENT_RECORD);
    });

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
    assertTrue(exception.getMessage().contains("Falha ao serializar PaymentRequest"));
  }
}