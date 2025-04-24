package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderItemDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BusinessException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.OrderHistoryMapper;
import com.rafaeldsal.ws.minhaprata.mapper.OrderItemMapper;
import com.rafaeldsal.ws.minhaprata.mapper.OrderMapper;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderHistory;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderHistoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderItemRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final UserRepository userRepository;

  private final OrderRepository orderRepository;

  private final OrderItemRepository orderItemRepository;

  private final ProductRepository productRepository;

  private final OrderItemMapper orderItemMapper;

  private final OrderHistoryRepository orderHistoryRepository;

  @Override
  public Order findById(Long id) {
    return null;
  }

  @Override
  public Page<OrderResponseDto> findAll(Integer page, Integer size, String sort) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtils.getSortDirection(sort), "dtOder"));
    Page<Order> orders = orderRepository.findAll(pageable);

    return orders.map(OrderMapper::entityToResponseDto);
  }

  @Override
  public Page<OrderResponseDto> findAllByUserId(Integer page, Integer size, String sort, Long userId) {
    var userExisting = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("Usuário não localizados"));

    Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtils.getSortDirection(sort), "dtOrder"));
    Page<Order> orders = orderRepository.findAllByUserId(userExisting.getId(), pageable);
    return orders.map(OrderMapper::entityToResponseDto);
  }

  @Override
  @Transactional
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

    OrderHistory orderHistory = OrderHistoryMapper.toEntity(order, user, "Pedido criado com sucesso");
    orderHistoryRepository.save(orderHistory);

    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  @Transactional
  public OrderResponseDto updateCartItems(OrderDto dto, Long id) {

    var user = userRepository.findById(dto.userId())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

    var order = orderRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

    validateOrderUpdate(order, dto.userId());

    List<OrderItem> updatedOrderItems = dto.orderItems().stream()
        .map(itemDto -> getOrCreateOrderItem(itemDto, order))
        .peek(item -> item.setOrder(order))
        .toList();

    BigDecimal totalPrice = calculateTotalPrice(updatedOrderItems);

    orderItemRepository.deleteAllByOrderId(order.getId());
    order.setOrderItems(updatedOrderItems);
    order.setTotalPrice(totalPrice);
    order.setUser(user);
    order.setDtUpdated(LocalDateTime.now());

    orderRepository.save(order);

    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  @Transactional
  public OrderResponseDto updateOrderStatus(OrderStatus orderStatus, Long orderId) {

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

    if (order.getStatus().equals(orderStatus)) {
      throw new BusinessException("Pedido já se encontra nesse status " + order.getStatus());
    }

    if (OrderStatus.PAID.equals(order.getStatus()) && OrderStatus.CANCELLED.equals(orderStatus)) {
      throw new BusinessException("Pedido não pode ser cancelado.");
    }

    order.setStatus(orderStatus);
    orderRepository.save(order);

    List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

    if (OrderStatus.CANCELLED.equals(orderStatus)) {
      updateStock(orderItems, false);
    } else if (OrderStatus.PAID.equals(orderStatus)) {
      updateStock(orderItems, true);
    }

    return OrderMapper.entityToResponseDto(order);
  }

  private void updateStock(List<OrderItem> items, boolean increment) {
    for (OrderItem item : items) {
      Product product = item.getProduct();
      product.setStockQuantity(increment ?
          product.getStockQuantity() + item.getQuantity()
          : product.getStockQuantity() - item.getQuantity());
      productRepository.save(product);
    }
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
