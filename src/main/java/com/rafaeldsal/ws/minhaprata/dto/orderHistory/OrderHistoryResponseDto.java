package com.rafaeldsal.ws.minhaprata.dto.orderHistory;

import com.rafaeldsal.ws.minhaprata.dto.order.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderHistoryResponseDto(
    String id,
    OrderStatus status,
    LocalDateTime dtEvent,
    LocalDateTime dtCreated,
    String note,
    OrderResponseDto order,
    String userId,
    String name,
    UserRole role
) {
}
