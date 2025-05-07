package com.rafaeldsal.ws.minhaprata.dto.payment;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(
    String userId,
    String orderId,
    String paymentMethod,
    String currency,
    Long amount
) {
}
