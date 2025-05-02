package com.rafaeldsal.ws.minhaprata.dto.payment;

public sealed interface PaymentResponse permits PaymentMethodPixResponse, PaymentMethodBoletoResponse, PaymentMethodCardResponse{
}
