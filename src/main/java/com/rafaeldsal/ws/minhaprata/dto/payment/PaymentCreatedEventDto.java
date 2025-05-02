package com.rafaeldsal.ws.minhaprata.dto.payment;

import com.rafaeldsal.ws.minhaprata.dto.order.OrderEventPaymentDto;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentCreatedEventDto(
    String requestId,
    String timestamp,
    OrderEventPaymentDto order,
    String paymentMethod,
    BigDecimal amount,
    String currency,
    SensitiveCardDataDto infoCard
) {
}
