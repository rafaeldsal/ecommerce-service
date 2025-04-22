package com.rafaeldsal.ws.minhaprata.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.jpa.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  
}
