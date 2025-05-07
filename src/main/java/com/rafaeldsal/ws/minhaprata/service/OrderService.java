package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.order.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import org.springframework.data.domain.Page;

public interface OrderService {

  OrderResponseDto findById(String orderIs);

  Page<OrderResponseDto> findAll(int page, int size, String sort, String userId, OrderStatus orderStatus);

  OrderResponseDto create(OrderDto order);

  OrderResponseDto update(OrderStatus orderStatus, String orderId);

  void updateStatusFromWebhook(Order order, OrderStatus status);

  void handleExpiredOrder(String orderId);
}
