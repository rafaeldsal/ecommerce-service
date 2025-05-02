package com.rafaeldsal.ws.minhaprata.dto.payment;

import lombok.Builder;

@Builder
public record SensitiveCardDataDto(
    String cardNumber,
    String cardExpirationMonth,
    String cardExpirationYear,
    String cardSecurityCode,
    Integer installments
) {
}
