package com.rafaeldsal.ws.minhaprata.dto.product;

import com.rafaeldsal.ws.minhaprata.model.enums.ProductEventType;
import lombok.Builder;

@Builder
public record ProductCreatedEventDto(
    ProductEventType eventType,
    String timestamp,
    ProductDataDto data
) {
}
