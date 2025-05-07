package com.rafaeldsal.ws.minhaprata.dto.payment;

import lombok.Builder;

@Builder
public record PaymentInitResponseDto(
    String id,
    String transactionId,
    String status
) {
}
