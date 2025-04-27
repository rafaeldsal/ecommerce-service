package com.rafaeldsal.ws.minhaprata.dto.user;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserResponseDto(
    String id,
    String name,
    String email,
    String cpf,
    String phoneNumber,
    LocalDate dtBirth,
    LocalDateTime dtCreated,
    LocalDateTime dtUpdated
) {
}
