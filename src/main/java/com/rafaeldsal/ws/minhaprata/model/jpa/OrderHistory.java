package com.rafaeldsal.ws.minhaprata.model.jpa;

import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_order_history")
public class OrderHistory implements Serializable {

  @Id
  @Column(name = "history_id", unique = true, nullable = false, updatable = false)
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @Column(name = "dt_event", nullable = false)
  private LocalDateTime dtEvent;

  @Column(name = "dt_created_order", nullable = false)
  private LocalDateTime dtCreatedOrder;

  @Column(name = "dt_updated", nullable = false)
  private LocalDateTime dtUpdated;

  private String note;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
