package com.rafaeldsal.ws.minhaprata.dto.orderItem;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemDto(

    @NotNull(message = "não pode ser nulo")
    Integer quantity,

    @NotNull(message = "deve ser informado")
    @DecimalMin(value = "0.0", inclusive = false, message = "deve ser maior que zero")
    BigDecimal priceAtPurchase,

    @NotNull(message = "não pode ser nulo")
    String productId
) {
}
