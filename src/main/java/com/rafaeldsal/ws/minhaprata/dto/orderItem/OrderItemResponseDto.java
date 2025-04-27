package com.rafaeldsal.ws.minhaprata.dto.orderItem;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponseDto(
  String id,
  String productId,
  String productName,
  Integer quantity,
  BigDecimal priceAtPurchase
) {
}
