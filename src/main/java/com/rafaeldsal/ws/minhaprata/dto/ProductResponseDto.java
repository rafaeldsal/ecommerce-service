package com.rafaeldsal.ws.minhaprata.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ProductResponseDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    String imgUrl,
    LocalDate dtCreated,
    LocalDate dtUpdated,
    CategoryDto category
) {
}
