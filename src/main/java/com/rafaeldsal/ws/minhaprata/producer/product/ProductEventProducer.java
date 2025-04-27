package com.rafaeldsal.ws.minhaprata.producer.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final ObjectMapper objectMapper;

  public String sendMessage(ProductResponseDto responseDto) throws JsonProcessingException {
    String content = objectMapper.writeValueAsString(responseDto);
    kafkaTemplate.send("", content);
    return "Produto enviado";
  }
}
