package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentInitResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout/payment")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping
  public ResponseEntity<PaymentInitResponseDto> create(@Valid @RequestBody PaymentDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(dto));
  }

  @GetMapping("/{transactionId}/status")
  public ResponseEntity<PaymentUpdateStatus> findPaymentIntentStatus(@PathVariable("transactionId") String transactionId) {
    return ResponseEntity.status(HttpStatus.OK).body(paymentService.findPaymentIntentStatus(transactionId));
  }
}
