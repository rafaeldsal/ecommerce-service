package com.rafaeldsal.ws.minhaprata.dto.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CardDetailsDto(
    String id,

    @NotBlank(message = "Número do cartão é obrigatório")
    @Pattern(regexp = "\\d{16}", message = "Número do cartão deve conter 16 dígitos numéricos")
    String cardNumber,

    @NotNull(message = "Mês de expiração é obrigatório")
    @Min(value = 1, message = "Mês deve estar entre 1 e 12")
    @Max(value = 12, message = "Mês deve estar entre 1 e 12")
    Integer cardExpirationMonth,

    @NotNull(message = "Ano de expiração é obrigatório")
    @Min(value = 2024, message = "Ano de expiração inválido")
    Integer cardExpirationYear,

    @NotBlank(message = "Código de segurança é obrigatório")
    @Pattern(regexp = "\\d{3,4}", message = "Código de segurança deve ter 3 ou 4 dígitos")
    String cardSecurityCode,

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    BigDecimal price,

    @NotNull(message = "Número de parcelas é obrigatório")
    @Min(value = 1, message = "Parcelas deve ser no mínimo 1")
    Integer installments
) {
}
