package com.rafaeldsal.ws.minhaprata.dto.order;

import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemResponseDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderEventPaymentDto(
    String orderId,
    String userId,
    BigDecimal totalPrice,
    List<OrderItemResponseDto> orderItem
) {
}
