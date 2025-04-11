package com.rafaeldsal.ws.minhaprata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.OrderItem;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  Optional<OrderItem> findByProductIdAndOrderId(Long productId, Long orderId);

  void deleteAllByOrderId(Long orderId);
  
}
