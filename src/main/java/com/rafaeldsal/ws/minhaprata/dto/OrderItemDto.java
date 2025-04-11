package com.rafaeldsal.ws.minhaprata.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemDto(

    @NotNull(message = "não pode ser nulo")
    Integer quantity,

    @NotNull(message = "deve ser informado")
    @DecimalMin(value = "0.0", inclusive = false, message = "deve ser maior que zero")
    BigDecimal priceAtPurchase,

    @NotNull(message = "não pode ser nulo")
    Long productId
) {
}
