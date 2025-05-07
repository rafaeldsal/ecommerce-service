package com.rafaeldsal.ws.minhaprata.dto.payment;

import lombok.Builder;

@Builder
public record PaymentRecord(
    String transactionId,
    String timestamp,
    String userId,
    String orderId,
    String paymentMethod,
    String currency,
    Long amount
) {
}
