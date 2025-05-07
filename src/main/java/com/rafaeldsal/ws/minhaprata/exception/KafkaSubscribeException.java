package com.rafaeldsal.ws.minhaprata.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KafkaSubscribeException extends RuntimeException {

  private final HttpStatus status;

  public KafkaSubscribeException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
