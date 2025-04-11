package com.rafaeldsal.ws.minhaprata.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponseDto(
  Long id,
  Long productId,
  String productName,
  Integer quantity,
  BigDecimal priceAtPurchase
) {
}
