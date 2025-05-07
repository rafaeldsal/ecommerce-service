package com.rafaeldsal.ws.minhaprata.model.jpa;

import com.rafaeldsal.ws.minhaprata.model.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "tbl_payment")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment implements Serializable {

  @Id
  @Column(name = "payment_id", unique = true, nullable = false, updatable = false)
  private String id;

  @Column(name = "dt_created", nullable = false)
  private LocalDateTime dtCreated;

  @Column(name = "dt_updated", nullable = false)
  private LocalDateTime dtUpdated;

  @Column(name = "payment_method", nullable = false)
  private String paymentMethod;

  @Column(nullable = false)
  private String currency;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(name = "save_payment_method")
  private Boolean savePaymentMethod;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "payment_intent_id")
  private String paymentIntentId;

  @Column(name = "client_secret")
  private String clientSecret;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  @Column(name = "failure_code")
  private String failureCode;

  @Column(name = "failure_message")
  private String failureMessage;

  @Column(name = "decline_code")
  private String declineCode;

  @Column(name = "failed_at")
  private LocalDateTime failedAt;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;
}
