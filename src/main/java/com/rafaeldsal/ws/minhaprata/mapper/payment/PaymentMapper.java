package com.rafaeldsal.ws.minhaprata.mapper.payment;

import com.rafaeldsal.ws.minhaprata.dto.order.OrderEventPaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentCreatedEventDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.SensitiveCardDataDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.Payment;
import com.rafaeldsal.ws.minhaprata.utils.DateTimeUtils;
import com.rafaeldsal.ws.minhaprata.utils.EncryptionUtils;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;

public class PaymentMapper {

  private PaymentMapper() {}

  public static Payment toEntity(PaymentDto dto, Order order) {
    return Payment.builder()
        .id(IdGenerator.UUIDGenerator("pay"))
        .amount(order.getTotalPrice())
        .paymentMethod(dto.paymentMethod())
        .order(order)
        .currency(dto.currency())
        .dtPayment(DateTimeUtils.now())
        .savePaymentMethod(dto.savePaymentMethod())
        .build();
  }

  public static SensitiveCardDataDto toSensitiveInfoCard(PaymentDto dto) throws Exception {
    if (dto.cardDetailsDto() == null) {
      return null;
    }

    return SensitiveCardDataDto.builder()
        .cardExpirationYear(EncryptionUtils.encrypt(String.valueOf(dto.cardDetailsDto().cardExpirationYear())))
        .cardExpirationMonth(EncryptionUtils.encrypt(String.valueOf(dto.cardDetailsDto().cardExpirationMonth())))
        .cardNumber(EncryptionUtils.encrypt(dto.cardDetailsDto().cardNumber()))
        .cardSecurityCode(EncryptionUtils.encrypt(dto.cardDetailsDto().cardSecurityCode()))
        .installments(dto.cardDetailsDto().installments())
        .build();
  }

  public static PaymentCreatedEventDto toEventDto(String requestId, OrderEventPaymentDto order, SensitiveCardDataDto infoCard, PaymentDto dto) {
    return PaymentCreatedEventDto.builder()
        .requestId(requestId)
        .infoCard(infoCard)
        .paymentMethod(dto.paymentMethod())
        .amount(dto.amount())
        .currency(dto.currency())
        .order(order)
        .timestamp(DateTimeUtils.timestamp())
        .build();
  }
}