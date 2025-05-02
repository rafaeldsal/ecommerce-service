package com.rafaeldsal.ws.minhaprata.dto.payment;

import java.math.BigDecimal;

public record PaymentMethodPixResponse(
    String requestId,
    String orderId,
    String paymentMethod,
    String qrCodeUrl,
    String qrCodeText,
    String expiresAt,
    BigDecimal amount
) implements PaymentResponse {
}
