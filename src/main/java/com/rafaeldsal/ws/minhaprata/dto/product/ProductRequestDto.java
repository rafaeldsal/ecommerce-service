package com.rafaeldsal.ws.minhaprata.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductRequestDto(

    String id,

    @NotBlank(message = "não pode ser nulo ou vazio")
    @Size(min = 3, message = "valor mínimo igual a 3")
    String name,

    @NotBlank(message = "não pode ser nulo ou vazio")
    String description,

    @NotNull(message = "deve ser informado")
    @DecimalMin(value = "0.0", inclusive = false, message = "deve ser maior que zero")
    BigDecimal price,

    String imgUrl,

    Long stockQuantity,

    @NotEmpty(message = "não pode ser nulo ou vazio")
    String currency,

    LocalDateTime dtCreated,

    LocalDateTime dtUpdated,

    @NotNull(message = "deve ser informado")
    String categoryId
) {
}
