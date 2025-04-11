package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderItemDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BusinessException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.OrderItemMapper;
import com.rafaeldsal.ws.minhaprata.mapper.OrderMapper;
import com.rafaeldsal.ws.minhaprata.model.Order;
import com.rafaeldsal.ws.minhaprata.model.OrderItem;
import com.rafaeldsal.ws.minhaprata.model.OrderStatus;
import com.rafaeldsal.ws.minhaprata.repository.OrderItemRepository;
import com.rafaeldsal.ws.minhaprata.repository.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private OrderItemMapper orderItemMapper;

  @Override
  public Order findById(Long id) {
    return null;
  }

  @Override
  public List<Order> findAll() {
    return List.of();
  }

  @Override
  public List<Order> findAllByUserId(Long id) {
    return List.of();
  }

  @Override
  public OrderResponseDto create(OrderDto dto) {
    var user = userRepository.findById(dto.userId())
        .orElseThrow(() -> new NotFoundException("Usuário não localizado"));

    List<OrderItem> orderItems = new ArrayList<>();

    for (OrderItemDto itemDto : dto.orderItems()) {
      OrderItem item = orderItemMapper.toEntity(itemDto);
      orderItems.add(item);
    }

    BigDecimal totalPrice = calculateTotalPrice(orderItems);

    Order order = Order.builder()
        .user(user)
        .totalPrice(totalPrice)
        .status(OrderStatus.PENDING)
        .orderItems(new ArrayList<>())
        .dtOrder(LocalDateTime.now())
        .dtUpdated(LocalDateTime.now())
        .build();

    for (OrderItem item : orderItems) {
      item.setOrder(order);
    }

    order.setOrderItems(orderItems);
    orderRepository.save(order);

    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  public OrderResponseDto update(OrderDto dto, Long id) {

    var user = userRepository.findById(dto.userId())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

    var order = orderRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

    validateOrderUpdate(order, dto.userId());

    List<OrderItem> updatedOrderItems = new ArrayList<>();

    for (OrderItemDto itemDto : dto.orderItems()) {
      OrderItem item  = getOrCreateOrderItem(itemDto, order);
      updatedOrderItems.add(item);
    }

    BigDecimal totalPrice = calculateTotalPrice(updatedOrderItems);

    order.setTotalPrice(totalPrice);
    order.setUser(user);
    order.setDtUpdated(LocalDateTime.now());

    orderItemRepository.deleteAllByOrderId(order.getId());
    order.setOrderItems(updatedOrderItems);

    for (OrderItem item : updatedOrderItems) {
      item.setOrder(order);
    }

    orderRepository.save(order);

    return OrderMapper.entityToResponseDto(order);
  }

  private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private OrderItem getOrCreateOrderItem(OrderItemDto itemDto, Order order) {
    var orderItem = orderItemRepository.findByProductIdAndOrderId(itemDto.productId(), order.getId());

    return orderItem
        .map(existing -> orderItemMapper.updateEntity(existing, itemDto))
        .orElseGet(() -> orderItemMapper.toEntity(itemDto));
  }

  private void validateOrderUpdate(Order order, Long userId) {
    if (!order.getUser().getId().equals(userId)) {
      throw new BusinessException("Pedido não pertence ao usuário informado");
    }

    if (!order.getStatus().equals(OrderStatus.PENDING)) {
      throw new BusinessException("Pedido não pode ser alterado. Status atual: " + order.getStatus());
    }
  }
}
