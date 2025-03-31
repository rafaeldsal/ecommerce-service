package com.rafaeldsal.ws.minhaprata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  
}
