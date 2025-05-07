package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.configuration.security.RequireAuthentication;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemDto;
import com.rafaeldsal.ws.minhaprata.exception.BusinessException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.order.OrderMapper;
import com.rafaeldsal.ws.minhaprata.mapper.orderHistory.OrderHistoryMapper;
import com.rafaeldsal.ws.minhaprata.mapper.orderItem.OrderItemMapper;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderHistory;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.model.redis.PendingOrder;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderItemRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.OrderHistoryService;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import com.rafaeldsal.ws.minhaprata.utils.DateTimeUtils;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;
  private final OrderItemMapper orderItemMapper;
  private final OrderHistoryService orderHistoryService;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public OrderResponseDto findById(String orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  public Page<OrderResponseDto> findAll(int page, int size, String sort, String userId, OrderStatus status) {
    Sort sortRequest = Sort.by(SortUtils.getSortDirection(sort));
    Pageable pageable = PageRequest.of(page, size, sortRequest);

    Page<Order> orders;

    if (userId != null && status != null) {
      orders = orderRepository.findAllByUserIdAndStatus(userId, status, pageable);
    } else if (userId != null) {
      orders = orderRepository.findAllByUserId(userId, pageable);
    } else if (status != null) {
      orders = orderRepository.findAllByStatus(status, pageable);
    } else {
      orders = orderRepository.findAll(pageable);
    }

    List<OrderResponseDto> orderResponseList = OrderMapper.entityListToResponseDtoList(orders.getContent());
    return new PageImpl<>(orderResponseList, pageable, orders.getTotalElements());
  }

  @Override
  @Transactional
  @RequireAuthentication
  public OrderResponseDto create(OrderDto dto) {
    var user = userRepository.findById(dto.userId())
        .orElseThrow(() -> new NotFoundException("Usuário não localizado"));

    Order order = Order.builder()
        .user(user)
        .status(OrderStatus.PENDING)
        .orderItems(new ArrayList<>())
        .dtOrder(DateTimeUtils.now())
        .dtUpdated(DateTimeUtils.now())
        .build();

    BigDecimal totalPrice = BigDecimal.ZERO;

    for (OrderItemDto itemDto : dto.orderItems()) {
      Product product = productRepository.findById(itemDto.productId()).orElseThrow(() -> new NotFoundException("Produto não encontrado"));

      product.decreaseStockOrFail(itemDto.quantity());
      // Persiste o produto após alteração de estoque para garantir sincronização imediata
      productRepository.save(product);

      OrderItem orderItem = orderItemMapper.toEntity(order, itemDto, product);
      order.getOrderItems().add(orderItem);

      totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
    }

    order.setTotalPrice(totalPrice);
    order.setId(IdGenerator.UUIDGenerator("order"));
    orderRepository.save(order);

    PendingOrder pendingOrder = PendingOrder.builder()
        .orderId(order.getId())
        .email(user.getEmail())
        .status(order.getStatus())
        .userId(user.getId())
        .createdAt(DateTimeUtils.now()).build();

    redisTemplate.opsForValue().set("order:" + order.getId(), pendingOrder, Duration.ofSeconds(60));

    OrderHistory orderHistory = OrderHistoryMapper.toEntity(order, user, "Pedido criado com sucesso");
    orderHistoryService.create(orderHistory);

    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  @Transactional
  public void handleExpiredOrder(String orderId) {
    Order order = orderRepository.findByIdWithOrderItems(orderId).orElseThrow(() -> new NotFoundException("Pedido não localizado"));

    if (order.getStatus() != OrderStatus.PENDING) {
      log.info("Pedido {} já processado com status {}", orderId, order.getStatus());
      return;
    }

    expireOrder(order);

    OrderHistory orderHistory = OrderHistoryMapper.toEntity(order, order.getUser(), "Pedido expirado automaticamente");
    orderHistoryService.create(orderHistory);
  }

  @Override
  @Transactional
  public OrderResponseDto update(OrderStatus orderStatus, String orderId) {

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

    if (OrderStatus.CANCELLED.equals(orderStatus)) {
      updateStockFromOrder(order, false, false);
    } else if (OrderStatus.PAID.equals(orderStatus)) {
      updateStockFromOrder(order, true, false);
    }

    orderHistoryService.create(OrderHistoryMapper.toEntity(order, order.getUser(), "Atualizando pedido pela tela de ADM"));

    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  @Transactional
  public void updateStatusFromWebhook(Order order, OrderStatus status) {
    if ((OrderStatus.PAID.equals(order.getStatus()) ||
        OrderStatus.DELIVERED.equals(order.getStatus())) &&
        OrderStatus.CANCELLED.equals(status)) {
      throw new BusinessException("Pedido não pode ser cancelado.");
    }

    if (order.getStatus().equals(status)) {
      throw new BusinessException("Pedido já se encontra nesse status " + order.getStatus());
    }

    order.setStatus(status);
    order.setDtUpdated(DateTimeUtils.now());
    orderRepository.save(order);

    orderHistoryService.create(OrderHistoryMapper.toEntity(order, order.getUser(), "Status alterado via webhook de pagamento"));
  }

  private void expireOrder(Order order) {
    updateStockFromOrder(order, true, true);
    order.setStatus(OrderStatus.EXPIRED);
    order.setDtUpdated(DateTimeUtils.now());
    orderRepository.save(order);
  }

  private void updateStockFromOrder(Order order, boolean increment, boolean usePessimisticLock) {
    List<OrderItem> orderItems = order.getOrderItems();
    List<Product> products;

    if (usePessimisticLock) {
      List<String> productIds = orderItems.stream()
          .map(item -> item.getProduct().getId())
          .toList();

      products = productRepository.findAllByForUpdate(productIds);
    } else {
      products = orderItems.stream()
          .map(OrderItem::getProduct)
          .toList();
    }

    Map<String, OrderItem> orderItemMap = orderItems.stream()
        .collect(Collectors.toMap(item ->
                item.getProduct().getId(),
            item -> item));

    for (Product product : products) {
      OrderItem orderItem = orderItemMap.get(product.getId());
      if (orderItem == null) {
        throw new IllegalStateException("Produto sem item correspondente");
      }

      long newStock = increment ?
          product.getStockQuantity() + orderItem.getQuantity() :
          product.getStockQuantity() - orderItem.getQuantity();

      if (newStock < 0) {
        throw new BusinessException("Estoque insuficiente para o produto: " + product.getId());
      }

      product.setStockQuantity(newStock);
    }

    productRepository.saveAllAndFlush(products);
  }
}
