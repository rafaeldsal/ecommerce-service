package com.rafaeldsal.ws.minhaprata.dto;

import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponseDto(
    Long id,
    LocalDateTime dtOrder,
    LocalDateTime dtUpdated,
    OrderStatus status,
    BigDecimal totalPrice,
    Long userId,
    List<OrderItemResponseDto> itemResponseDto
) {
}
