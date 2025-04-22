package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {

  Order findById(Long id);

  Page<OrderResponseDto> findAll(Integer page, Integer size, String sort);

  Page<OrderResponseDto> findAllByUserId(Integer page, Integer size, String sort, Long id);

  OrderResponseDto create(OrderDto order);

  OrderResponseDto updateCartItems(OrderDto order, Long id);

  OrderResponseDto updateOrderStatus(OrderStatus status, Long orderId);
}
