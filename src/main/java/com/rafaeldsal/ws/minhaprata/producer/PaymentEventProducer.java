package com.rafaeldsal.ws.minhaprata.producer;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentCreatedEventDto;

public interface PaymentEventProducer {
  void sendMessage(PaymentCreatedEventDto dto);
}
