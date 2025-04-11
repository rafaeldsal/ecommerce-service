package com.rafaeldsal.ws.minhaprata.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserResponseDto(
    Long id,
    String name,
    String email,
    String cpf,
    String phoneNumber,
    LocalDate dtBirth,
    LocalDateTime dtCreated,
    LocalDateTime dtUpdated
) {
}
