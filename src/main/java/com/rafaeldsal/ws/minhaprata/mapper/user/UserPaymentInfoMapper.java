package com.rafaeldsal.ws.minhaprata.mapper.user;

import com.rafaeldsal.ws.minhaprata.dto.payment.CardDetailsDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserPaymentInfo;
import com.rafaeldsal.ws.minhaprata.utils.DateTimeUtils;
import com.rafaeldsal.ws.minhaprata.utils.EncryptionUtils;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;

public class UserPaymentInfoMapper {

  private UserPaymentInfoMapper() { }

  public static UserPaymentInfo toEntity(PaymentDto dto, User user) throws Exception {
    return UserPaymentInfo.builder()
        .id(IdGenerator.UUIDGenerator("user-payment-info"))
        .dtPayment(DateTimeUtils.now())
        .cardNumber(EncryptionUtils.encrypt(dto.cardDetailsDto().cardNumber()))
        .cardSecurityCode(EncryptionUtils.encrypt(dto.cardDetailsDto().cardSecurityCode()))
        .cardExpirationMonth(EncryptionUtils.encrypt(String.valueOf(dto.cardDetailsDto().cardExpirationMonth())))
        .cardExpirationYear(EncryptionUtils.encrypt(String.valueOf(dto.cardDetailsDto().cardExpirationYear())))
        .price(dto.cardDetailsDto().price())
        .savePaymentMethod(dto.savePaymentMethod())
        .installments(dto.cardDetailsDto().installments())
        .user(user)
        .build();
  }
}
