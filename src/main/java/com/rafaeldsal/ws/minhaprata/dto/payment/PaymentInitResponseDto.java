package com.rafaeldsal.ws.minhaprata.dto.payment;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponseDto(
    String id,
    String transactionId,
    String paymentMethod,
    String status,
    BigDecimal amount,
    String paymentIntentId,
    String orderId,
    String userId,
    String currency,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
