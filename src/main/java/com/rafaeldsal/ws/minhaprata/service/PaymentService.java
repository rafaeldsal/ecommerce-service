package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Payment;

public interface PaymentService {

  Payment create(PaymentDto dto);
}
