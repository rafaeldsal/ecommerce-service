package com.rafaeldsal.ws.minhaprata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  
}
