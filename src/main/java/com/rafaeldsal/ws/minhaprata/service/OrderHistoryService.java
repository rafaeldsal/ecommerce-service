package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.OrderHistoryResponseDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderHistory;
import org.springframework.data.domain.Page;

public interface OrderHistoryService {

  void create(OrderHistory orderHistory);

  Page<OrderHistoryResponseDto> findAllHistoryByOrderId(Integer page, Integer size, String sort, Long orderId);

  Page<OrderHistoryResponseDto> findAllHistory(Integer page, Integer size, String sort);
}
