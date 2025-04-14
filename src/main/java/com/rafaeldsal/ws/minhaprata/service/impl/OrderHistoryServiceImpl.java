package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.OrderHistoryResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.OrderHistoryMapper;
import com.rafaeldsal.ws.minhaprata.repository.OrderHistoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.OrderRepository;
import com.rafaeldsal.ws.minhaprata.service.OrderHistoryService;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class OrderHistoryServiceImpl implements OrderHistoryService {

  @Autowired
  private OrderHistoryRepository orderHistoryRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public Page<OrderHistoryResponseDto> findAllHistoryByOrderId(Integer page, Integer size, String sort, Long orderId) {

    orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

    Pageable pageable = PageRequest.of(page, size, SortUtils.getSortDirection(sort), "dtEvent");
    var orderHistory = orderHistoryRepository.findAllHistoryByOrderId(orderId, pageable);

    return orderHistory.map(OrderHistoryMapper::fromEntityToDto);
  }

  @Override
  public Page<OrderHistoryResponseDto> findAllHistory(Integer page, Integer size, String sort) {
    Pageable pageable = PageRequest.of(page, size, SortUtils.getSortDirection(sort), "dtEvent");
    var orderHistory = orderHistoryRepository.findAll(pageable);

    return orderHistory.map(OrderHistoryMapper::fromEntityToDto);
  }
}
