package com.rafaeldsal.ws.minhaprata.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentIntentProducer {

  @Value("${webservices.minhaprata.kafka.topic.payment-intent}")
  private String topicPaymentIntent;

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;
}
