package com.rafaeldsal.ws.minhaprata.exception.handler;

import com.rafaeldsal.ws.minhaprata.dto.error.ErrorResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.BusinessException;
import com.rafaeldsal.ws.minhaprata.exception.HttpClientException;
import com.rafaeldsal.ws.minhaprata.exception.IntegrationException;
import com.rafaeldsal.ws.minhaprata.exception.KafkaSubscribeException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.exception.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponseDto> handlerUnauthorizedException(UnauthorizedException u) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponseDto.builder()
        .message(u.getMessage())
        .status(HttpStatus.UNAUTHORIZED)
        .statusCode(HttpStatus.UNAUTHORIZED.value())
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

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponseDto> handlerBusinessException(BusinessException b) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto.builder()
        .message(b.getMessage())
        .status(HttpStatus.CONFLICT)
        .statusCode(HttpStatus.CONFLICT.value())
        .build());
  }

  @ExceptionHandler(IntegrationException.class)
  public ResponseEntity<ErrorResponseDto> handlerIntegrationException(IntegrationException b) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(b.getMessage())
        .status(b.getStatus())
        .statusCode(b.getStatus().value())
        .build());
  }

  @ExceptionHandler(KafkaSubscribeException.class)
  public ResponseEntity<ErrorResponseDto> handlerKafkaSubscribeException(KafkaSubscribeException b) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorResponseDto.builder()
        .message(b.getMessage())
        .status(b.getStatus())
        .statusCode(b.getStatus().value())
        .build());
  }

  @ExceptionHandler(HttpClientException.class)
  public ResponseEntity<ErrorResponseDto> handlerHttpClientException(HttpClientException d) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(d.getMessage())
        .status(d.getStatus())
        .statusCode(d.getStatus().value())
        .build());
  }

}
