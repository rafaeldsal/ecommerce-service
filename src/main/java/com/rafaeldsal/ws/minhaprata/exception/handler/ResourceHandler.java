package com.rafaeldsal.ws.minhaprata.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rafaeldsal.ws.minhaprata.dto.error.ErrorResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;

@RestControllerAdvice
public class ResourceHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handlerNotFoundException(NotFoundException n) {
    String errorMessage = n.getMessage();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.NOT_FOUND)
        .statusCode(HttpStatus.NOT_FOUND.value())
        .build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponseDto> handlerBadRequestException(BadRequestException b) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(b.getMessage())
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .build());
  }

}
