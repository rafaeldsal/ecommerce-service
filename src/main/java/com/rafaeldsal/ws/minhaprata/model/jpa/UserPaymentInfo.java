package com.rafaeldsal.ws.minhaprata.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "tbl_payment_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserPaymentInfo implements Serializable {

  @Id
  @Column(name = "user_payment_info_id", nullable = false, unique = true)
  private String id;

  @Column(name = "card_number", nullable = false, unique = true)
  private String cardNumber;

  @Column(name = "card_expiration_month", nullable = false)
  private String cardExpirationMonth;

  @Column(name = "card_expiration_year", nullable = false)
  private String cardExpirationYear;

  @Column(name = "card_security_code", nullable = false)
  private String cardSecurityCode;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Integer installments;

  @Column(name = "dt_payment", nullable = false)
  private LocalDateTime dtPayment = LocalDateTime.now();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "save_payment_method")
  private boolean savePaymentMethod;
}
