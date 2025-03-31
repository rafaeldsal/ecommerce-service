package com.rafaeldsal.ws.minhaprata.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_payment_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPaymentInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_payment_info_id")
  private Integer id;

  @Column(name = "card_number")
  private String cardNumber;

  @Column(name = "card_expiration_month")
  private Integer cardExpirationMonth;

  @Column(name = "card_expiration_year")
  private Integer cardExpirationYear;

  @Column(name = "card_security_code")
  private String cardSecurityCode;

  private BigDecimal price;

  private Integer instalments;

  @Column(name = "dt_payment")
  private LocalDate dtPayment = LocalDate.now();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id")
  private User user;

}
