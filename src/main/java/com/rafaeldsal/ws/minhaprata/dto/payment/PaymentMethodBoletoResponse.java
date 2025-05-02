package com.rafaeldsal.ws.minhaprata.dto.payment;

import java.math.BigDecimal;

public record PaymentMethodBoletoResponse(
    String requestId,
    String orderId,
    String paymentMethod,
    String barcode,
    String boletoPdfUrl,
    String expiresAt,
    BigDecimal amount

) implements PaymentResponse {
}
