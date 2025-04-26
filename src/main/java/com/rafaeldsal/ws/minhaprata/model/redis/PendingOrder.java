package com.rafaeldsal.ws.minhaprata.model.redis;

import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
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
public class PendingOrder implements Serializable {

  private String orderId;
  private String email;
  private Long userId;
  private OrderStatus status;
  private LocalDateTime createdAt;
}
