package com.rafaeldsal.minha.prata.minha_prata.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_order_item")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, updatable = false)
  private Integer id;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private BigDecimal priceAtPurchase;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
}
