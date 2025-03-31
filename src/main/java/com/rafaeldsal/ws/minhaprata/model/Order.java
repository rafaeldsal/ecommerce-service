package com.rafaeldsal.ws.minhaprata.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_orders")
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, updatable = false)
  private Integer id;

  @Column(nullable = false)
  private LocalDate orderDate = LocalDate.now();

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> orderItems;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<Payment> payments;
}
