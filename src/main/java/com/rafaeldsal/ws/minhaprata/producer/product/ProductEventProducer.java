package com.rafaeldsal.ws.minhaprata.producer.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.mapper.product.ProductMapper;
import com.rafaeldsal.ws.minhaprata.model.enums.ProductEventType;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
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
public class ProductEventProducer {

  @Value("${webservices.minhaprata.kafka.topic.product-created}")
  private String topicProductCreated;

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final ObjectMapper objectMapper;

  public void sendMessage(Product product, ProductEventType eventType) throws JsonProcessingException {
    String content = objectMapper.writeValueAsString(ProductMapper.fromEntityToEventDto(product, eventType));

    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicProductCreated, content);
    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("Mensagem enviado com sucesso = [{}] - Com offset = [{}]", content, result.getRecordMetadata().offset());
      } else {
        log.error("Erro ao enviar mensagem = [{}] - devido a = [{}]", content, ex.getMessage());
      }
    });
  }
}
