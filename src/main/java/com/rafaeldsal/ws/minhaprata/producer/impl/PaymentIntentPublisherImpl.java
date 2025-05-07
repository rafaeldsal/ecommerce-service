package com.rafaeldsal.ws.minhaprata.producer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentRecord;
import com.rafaeldsal.ws.minhaprata.producer.PaymentIntentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentIntentPublisherImpl implements PaymentIntentPublisher {

  @Value("${webservices.minhaprata.kafka.topic.payment-intent-request}")
  private String topicPaymentCreated;

  private final Executor callbackExecutor;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void sendMessage(PaymentRecord dto) {
    String content = null;
    try {
      content = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      log.error("Erro ao serializar mensagem para Kafka: {}", e.getMessage());

      //KafkaPublishingException -> Vou criar depois
      throw new RuntimeException("Falha ao serializar PaymentRequest", e);
    }

    String key = dto.transactionId();

    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicPaymentCreated, key, content);
    future.whenCompleteAsync((result, ex) -> {
      if (ex == null) {
        log.info("Mensagem enviada com sucesso para o tópico [{}], offset [{}], partition [{}]",
            topicPaymentCreated,
            result.getRecordMetadata().offset(),
            result.getRecordMetadata().partition());
      } else {
        log.error("Erro ao enviar mensagem para o tópico [{}]: {}",
            topicPaymentCreated,
            ex.getMessage());
      }
    }, callbackExecutor);
  }
}
