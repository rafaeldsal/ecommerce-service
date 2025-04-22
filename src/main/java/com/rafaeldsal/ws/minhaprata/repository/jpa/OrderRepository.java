package com.rafaeldsal.ws.minhaprata.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.jpa.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Page<Order> findAllByUserId(Long userId, Pageable pageable);
  
}
