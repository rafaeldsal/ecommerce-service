package com.rafaeldsal.ws.minhaprata.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.exception.KafkaSubscribeException;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStatusUpdateConsumer {

  private final ObjectMapper objectMapper;
  private final PaymentService paymentService;

  @KafkaListener(
      topics = "${webservices.minhaprata.kafka.topic.payment-intent-response}",
      groupId = "payment-consumer-1")
  public void consumerPaymentIntentStatusUpdate(String message) {
    try {
      PaymentUpdateStatus payment = objectMapper.readValue(message, PaymentUpdateStatus.class);
      paymentService.update(payment);
    } catch (JsonProcessingException e) {
      log.error("Erro ao deserializar mensagem para Kafka: {}", e.getMessage());
      throw new KafkaSubscribeException("Falha ao deserializar PaymentRequest - ERROR - " + e, HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
}
