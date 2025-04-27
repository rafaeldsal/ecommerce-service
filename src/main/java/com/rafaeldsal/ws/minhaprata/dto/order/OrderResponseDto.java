package com.rafaeldsal.ws.minhaprata.dto.order;

import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponseDto(
    String id,
    LocalDateTime dtOrder,
    LocalDateTime dtUpdated,
    OrderStatus status,
    BigDecimal totalPrice,
    String userId,
    List<OrderItemResponseDto> itemResponseDto
) {
}
