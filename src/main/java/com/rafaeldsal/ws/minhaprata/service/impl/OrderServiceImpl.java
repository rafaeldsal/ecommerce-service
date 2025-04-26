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
import com.rafaeldsal.ws.minhaprata.model.redis.PendingOrder;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderItemRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.OrderHistoryService;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
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
import java.time.LocalDateTime;
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

  private final RedisTemplate<String, PendingOrder> redisTemplate;

  @Override
  public OrderResponseDto findById(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  public Page<OrderResponseDto> findAll(int page, int size, String sort, Long userId, OrderStatus status) {
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
  public OrderResponseDto create(OrderDto dto) {
    var user = userRepository.findById(dto.userId()).orElseThrow(() -> new NotFoundException("Usuário não localizado"));

    Order order = Order.builder().user(user).status(OrderStatus.PENDING).orderItems(new ArrayList<>()).dtOrder(LocalDateTime.now()).dtUpdated(LocalDateTime.now()).build();

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
    orderRepository.save(order);

    PendingOrder pendingOrder = PendingOrder.builder().orderId(order.getId().toString()).email(user.getEmail()).status(order.getStatus()).createdAt(LocalDateTime.now()).build();

    redisTemplate.opsForValue().set("order:" + order.getId(), pendingOrder, Duration.ofSeconds(60));

    OrderHistory orderHistory = OrderHistoryMapper.toEntity(order, user, "Pedido criado com sucesso");
    orderHistoryService.create(orderHistory);

    return OrderMapper.entityToResponseDto(order);
  }

  @Override
  @Transactional
  public OrderResponseDto update(OrderStatus orderStatus, Long orderId) {

    Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

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

  @Override
  @Transactional
  public void expireOrder(Long orderId) {
    Order order = orderRepository.findByIdWithOrderItems(orderId).orElseThrow(() -> new NotFoundException("Pedido não localizado"));

    if (order.getStatus() != OrderStatus.PENDING) {
      log.info("Pedido {} já processado com status {}", orderId, order.getStatus());
      return;
    }

    List<Long> productsId = order.getOrderItems().stream()
        .map(
            orderItem -> orderItem.getProduct().getId())
        .toList();

    Map<Long, OrderItem> orderItemMap = order.getOrderItems().stream()
        .collect(Collectors.toMap(
            item -> item.getProduct().getId(),
            item -> item)
        );

    List<Product> products = productRepository.findAllByForUpdate(productsId);

    products.forEach(product -> {
      OrderItem orderItem = orderItemMap.get(product.getId());
      if (orderItem == null) {
        throw new IllegalStateException("Produto sem item correspondente");
      }

      long quantity = orderItem.getQuantity();

      product.setStockQuantity(product.getStockQuantity() + quantity);
    });

    productRepository.saveAllAndFlush(products);

    order.setStatus(OrderStatus.EXPIRED);
    order.setDtUpdated(LocalDateTime.now());
    orderRepository.save(order);

    OrderHistory orderHistory = OrderHistoryMapper.toEntity(order, order.getUser(), "Pedido expirado automaticamente");
    orderHistoryService.create(orderHistory);
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
}
