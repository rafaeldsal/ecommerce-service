package com.rafaeldsal.ws.minhaprata.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_payment")
@Data
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private LocalDateTime paymentDate;

  @Column(nullable = false)
  private String paymentMethod;

  @Column(nullable = false)
  private BigDecimal amount;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

}
