package com.rafaeldsal.ws.minhaprata.dto.payment;

import lombok.Builder;

@Builder
public record PaymentWebhookResponseDto(
    String transactionId,
    String status,
    String timestamp,
    String paymentIntentId,
    PaymentErrorInfo paymentErrorInfo
) {
}
