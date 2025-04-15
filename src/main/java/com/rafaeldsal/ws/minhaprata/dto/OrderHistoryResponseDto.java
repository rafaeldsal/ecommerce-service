package com.rafaeldsal.ws.minhaprata.dto;

import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderHistoryResponseDto(
    Long id,
    OrderStatus status,
    LocalDateTime dtEvent,
    LocalDateTime dtCreated,
    String note,
    OrderResponseDto order,
    Long userId,
    String name,
    UserRole role
) {
}
