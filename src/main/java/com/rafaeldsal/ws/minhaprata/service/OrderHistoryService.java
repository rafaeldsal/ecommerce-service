package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.orderHistory.OrderHistoryResponseDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import org.springframework.data.domain.Page;

public interface OrderHistoryService {

  void create(Order order, User user, String note);

  Page<OrderHistoryResponseDto> findAllHistoryByOrderId(Integer page, Integer size, String sort, String orderId);

  Page<OrderHistoryResponseDto> findAllHistory(Integer page, Integer size, String sort);
}
