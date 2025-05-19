package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.orderHistory.OrderHistoryResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.orderHistory.OrderHistoryMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderHistoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.service.OrderHistoryService;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {

  private final OrderHistoryRepository orderHistoryRepository;
  private final OrderRepository orderRepository;

  @Override
  public void create(Order order, User user, String note) {

    if (order == null) {
      throw new BadRequestException("Pedido não pode ser nulo");
    }

    if (user == null) {
      throw new BadRequestException("Usuário não pode ser nulo");
    }

    orderHistoryRepository.save(OrderHistoryMapper.toEntity(order, user, note));
  }

  @Override
  public Page<OrderHistoryResponseDto> findAllHistoryByOrderId(Integer page, Integer size, String sort, String orderId) {

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
