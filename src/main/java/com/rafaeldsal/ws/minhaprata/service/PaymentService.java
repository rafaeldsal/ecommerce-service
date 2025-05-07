package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentInitResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentWebhookResponseDto;

public interface PaymentService {


  PaymentInitResponseDto create(PaymentDto dto);
  void update(PaymentUpdateStatus dto);
  void updateStatusPaymentCompleted(PaymentWebhookResponseDto dto);
  PaymentUpdateStatus findPaymentIntentStatus(String transactionId);
}
