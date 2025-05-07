package com.rafaeldsal.ws.minhaprata.producer;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentRecord;

public interface PaymentIntentPublisher {
  void sendMessage(PaymentRecord dto);
}
