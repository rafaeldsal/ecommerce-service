package com.rafaeldsal.ws.minhaprata.dto.product;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDataDto(
    String productId,
    String name,
    String description,
    String currency,
    BigDecimal price,
    Long stockQuantity,
    String imageUrl
) {
}
