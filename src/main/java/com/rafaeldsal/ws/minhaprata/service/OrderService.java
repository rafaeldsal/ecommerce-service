package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.Order;

import java.util.List;

public interface OrderService {

  Order findById(Long id);

  List<Order> findAll();

  List<Order> findAllByUserId(Long id);

  OrderResponseDto create(OrderDto order);

  OrderResponseDto update(OrderDto order, Long id);
}
