package com.rafaeldsal.ws.minhaprata.dto.product;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponseDto(
    String id,
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
