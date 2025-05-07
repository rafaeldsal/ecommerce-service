package com.rafaeldsal.ws.minhaprata.exception;

public class KafkaSubscribeException extends RuntimeException {
  public KafkaSubscribeException(String message) {
    super(message);
  }
}
