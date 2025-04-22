package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  Optional<OrderItem> findByProductIdAndOrderId(Long productId, Long orderId);

  List<OrderItem> findAllByOrderId(Long orderId);

  void deleteAllByOrderId(Long orderId);
  
}
