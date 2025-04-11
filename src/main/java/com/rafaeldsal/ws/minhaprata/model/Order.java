package com.rafaeldsal.ws.minhaprata.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_order")
public class Order implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", unique = true, nullable = false, updatable = false)
  private Long id;

  @Column(name = "dt_order", nullable = false)
  private LocalDateTime dtOrder = LocalDateTime.now();

  @Column(name = "dt_updated", nullable = false)
  private LocalDateTime dtUpdated = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @Column(name = "total_price")
  private BigDecimal totalPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<Payment> payments =  new ArrayList<>();
}
