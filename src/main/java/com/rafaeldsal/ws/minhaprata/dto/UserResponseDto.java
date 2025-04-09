package com.rafaeldsal.ws.minhaprata.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponseDto(
    Long id,
    String name,
    String email,
    String cpf,
    String phoneNumber,
    LocalDate dtBirth,
    LocalDate dtCreated,
    LocalDate dtUpdated
) {
}
