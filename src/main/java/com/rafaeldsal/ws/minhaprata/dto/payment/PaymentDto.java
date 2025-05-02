package com.rafaeldsal.ws.minhaprata.dto.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentDto(
    String id,

    LocalDateTime dtPayment,

    @NotBlank(message = "é preciso informar o método de pagamento")
    String paymentMethod,

    @NotNull(message = "valor do pagamento é obrigatório")
    BigDecimal amount,

    @NotBlank(message = "não poder nulo ou vazio")
    String currency,

    @NotBlank(message = "não pode ser nulo ou vazio")
    String orderId,

    boolean savePaymentMethod,

    @Valid
    CardDetailsDto cardDetailsDto
) {
    @AssertTrue(message = "Informações do cartão são obrigatórias para pagamentos com o cartão")
    public boolean isCardDetailsValid() {
        if ("card".equalsIgnoreCase(paymentMethod)) {
            return cardDetailsDto != null;
        } else {
            return cardDetailsDto == null && !savePaymentMethod;
        }
    }
}
