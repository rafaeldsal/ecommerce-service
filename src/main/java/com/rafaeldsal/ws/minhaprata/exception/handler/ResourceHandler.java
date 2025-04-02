package com.rafaeldsal.ws.minhaprata.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;

@RestControllerAdvice
public class ResourceHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handlerNotFoundException(NotFoundException n) {
    String errorMessage = n.getMessage();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handlerBadRequestException(BadRequestException b) {
    String errorMessage = b.getMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }

}
