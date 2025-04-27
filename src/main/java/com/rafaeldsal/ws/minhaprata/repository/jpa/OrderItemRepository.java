package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

  Optional<OrderItem> findByProductIdAndOrderId(String productId, String orderId);

  List<OrderItem> findAllByOrderId(String orderId);

  void deleteAllByOrderId(String orderId);
  
}
