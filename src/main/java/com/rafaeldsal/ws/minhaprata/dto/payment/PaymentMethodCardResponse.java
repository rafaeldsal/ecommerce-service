package com.rafaeldsal.ws.minhaprata.dto.payment;

import java.math.BigDecimal;

public record PaymentMethodCardResponse(
    String requestId,
    String orderId,
    String paymentMethod,
    String status,
    String transactionId,
    BigDecimal amount,
    String paidAt
) implements PaymentResponse {
}
