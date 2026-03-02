package com.rafaeldsal.ws.minhaprata.exception;

public class ApiException extends RuntimeException {
  public ApiException(String message) {
    super(message);
  }
}
