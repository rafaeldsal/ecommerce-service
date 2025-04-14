package com.rafaeldsal.ws.minhaprata.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponseDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    String imgUrl,
    Long stockQuantity,
    LocalDateTime dtCreated,
    LocalDateTime dtUpdated,
    CategoryDto category
) {
}
