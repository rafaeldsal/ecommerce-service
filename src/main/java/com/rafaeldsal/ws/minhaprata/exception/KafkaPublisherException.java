package com.rafaeldsal.ws.minhaprata.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KafkaPublisherException extends RuntimeException {

  private final HttpStatus status;

  public KafkaPublisherException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
