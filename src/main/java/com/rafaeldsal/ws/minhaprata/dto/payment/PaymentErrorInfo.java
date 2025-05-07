package com.rafaeldsal.ws.minhaprata.dto.payment;

import lombok.Builder;

@Builder
public record PaymentErrorInfo(
    String errorCode,
    String errorMessage,
    String stripeFailureCode
) {
}
