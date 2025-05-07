package com.rafaeldsal.ws.minhaprata.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentWebhookResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.KafkaSubscribeException;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusNotifierConsumer {

  private final ObjectMapper objectMapper;
  private final PaymentService paymentService;

  @KafkaListener(
      topics = "${webservices.minhaprata.kafka.topic.payment-intent-status-updated}",
      groupId = "payment-consumer-status-updated-1")
  public void consumerPaymentIntentStatusUpdatedByWebhook(String message) {
    try {
      PaymentWebhookResponseDto dto = objectMapper.readValue(message, PaymentWebhookResponseDto.class);
      log.info("Evento de pagamento enviado após validação do Webhook: {}", dto);
      paymentService.updateStatusPaymentCompleted(dto);
    } catch (JsonProcessingException e) {
      log.error("Erro ao deserializar mensagem para Kafka: {}", e.getMessage());
      throw new KafkaSubscribeException("Falha ao deserializar PaymentRequest - ERROR - " + e, HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
}
