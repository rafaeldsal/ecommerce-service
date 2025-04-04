package com.rafaeldsal.ws.minhaprata.exception.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rafaeldsal.ws.minhaprata.dto.error.ErrorResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ResourceHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handlerMethodArgumentNotValidException(MethodArgumentNotValidException m) {

    Map<String, String> messages = new HashMap<>();

    m.getBindingResult().getAllErrors().forEach(error -> {
      String field = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      messages.put(field, message);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(Arrays.toString(messages.entrySet().toArray()))
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .build());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handlerNotFoundException(NotFoundException n) {
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

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponseDto> dataIntegrityViolationException(DataIntegrityViolationException d) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(d.getMessage())
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .build());
  }

}
