package com.rafaeldsal.ws.minhaprata.configuration.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

  @Value("${webservices.minhaprata.kafka.topic.payment-intent}")
  private String topicPaymentIntent;

  @Value("${webservices.minhaprata.kafka.topic.product-created}")
  private String topicProductCreated;

  private final KafkaProperties kafkaProperties;

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> properties = kafkaProperties.buildProducerProperties();
    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public KafkaAdmin.NewTopics createdTopics() {
    return new KafkaAdmin.NewTopics(
        TopicBuilder.name(topicPaymentIntent)
            .partitions(1)
            .replicas(1)
            .build(),

        TopicBuilder.name(topicProductCreated)
            .partitions(1)
            .replicas(1)
            .build());

  }

}
