package com.rafaeldsal.ws.minhaprata.dto.error;

import org.springframework.http.HttpStatus;

import lombok.Builder;

@Builder
public record ErrorResponseDto(
  String message,
  HttpStatus status,
  Integer statusCode
) {
  
}
