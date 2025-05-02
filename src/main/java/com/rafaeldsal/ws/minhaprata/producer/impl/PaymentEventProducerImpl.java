package com.rafaeldsal.ws.minhaprata.producer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentCreatedEventDto;
import com.rafaeldsal.ws.minhaprata.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducerImpl implements PaymentEventProducer {

  @Value("${webservices.minhaprata.kafka.topic.payment-intent}")
  private String topicPaymentCreated;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void sendMessage(PaymentCreatedEventDto dto) {
    String content = null;
    try {
      content = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      log.error("Erro ao serializar mensagem para Kafka: {}", e.getMessage());
    }

    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicPaymentCreated, content);
    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("Mensagem enviada com sucesso para o tópico [ {} ] - Com offset [ {} ]", topicPaymentCreated, result.getRecordMetadata().offset());
      } else {
        log.error("Erro ao enviar mensagem para o tópico [ {} ] - devida a [ {} ]", topicPaymentCreated, ex.getMessage());
      }
    });
  }
}
