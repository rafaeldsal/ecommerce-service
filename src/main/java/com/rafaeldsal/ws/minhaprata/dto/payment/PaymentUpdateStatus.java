package com.rafaeldsal.ws.minhaprata.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PaymentUpdateStatus(
    String paymentId,
    @NotBlank
    String transactionId,
    @NotBlank
    String status,
    String orderId,
    String userId,
    @NotBlank
    String clientSecret,
    String paymentIntentId,
    PaymentErrorInfo paymentErrorInfo
) {
}
