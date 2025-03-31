package com.rafaeldsal.ws.minhaprata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.OrderHistory;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
  
}
