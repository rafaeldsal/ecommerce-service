package com.rafaeldsal.ws.minhaprata.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.jpa.OrderHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, String> {

  Page<OrderHistory> findAllHistoryByOrderId(String orderId, Pageable pageable);
  
}