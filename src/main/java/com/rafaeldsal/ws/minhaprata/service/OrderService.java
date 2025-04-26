package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {

  OrderResponseDto findById(Long orderIs);

  Page<OrderResponseDto> findAll(int page, int size, String sort, Long userId, OrderStatus orderStatus);

  OrderResponseDto create(OrderDto order);

  OrderResponseDto update(OrderStatus orderStatus, Long orderId);

  void expireOrder(Long orderId);
}
