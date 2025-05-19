package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderHistory;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderHistoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.OrderHistoryServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServiceTest {

  private static final String USER_ID = IdGenerator.UUIDGenerator("user");
  private static final String ORDER_ID = IdGenerator.UUIDGenerator("order");
  private static final String ORDER_HISTORY_ID = IdGenerator.UUIDGenerator("orderHistory");
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10, Sort.by(SortUtils.getSortDirection("ASC"), "dtEvent"));

  @Mock
  private OrderHistoryRepository orderHistoryRepository;

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private OrderHistoryServiceImpl orderHistoryService;

  private User loadUser() {
    return User.builder()
        .id(USER_ID)
        .name("Rafael Souza")
        .email("rafael@email.com")
        .cpf("12345678900")
        .phoneNumber("61999999999")
        .dtBirth(LocalDate.of(2025, 4, 23))
        .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .role(UserRole.USER)
        .build();
  }

  private Order loadOrder() {
    return Order.builder()
        .id(ORDER_ID)
        .user(loadUser())
        .status(OrderStatus.PENDING)
        .totalPrice(BigDecimal.valueOf(500.00))
        .dtOrder(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .orderItems(List.of())
        .build();
  }

  private OrderHistory loadOrderHistory(Order order) {
    return OrderHistory.builder()
        .id(ORDER_HISTORY_ID)
        .status(order.getStatus())
        .dtCreatedOrder(order.getDtOrder())
        .dtEvent(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .order(order)
        .user(loadUser())
        .note("Nota")
        .build();
  }

  @Test
  void given_create_when_orderAndUserAreValid_then_saveMappedOrderHistoryToRepository() {
    var order = loadOrder();
    var orderHistory = loadOrderHistory(order);

    when(orderHistoryRepository.save(any())).thenReturn(orderHistory);

    orderHistoryService.create(order, loadUser(), "Nota de teste");

    verify(orderHistoryRepository, times(1)).save(any());
  }

  @Test
  void given_create_when_orderIsNull_then_throwBadRequestException() {
    assertThrows(BadRequestException.class, () -> orderHistoryService.create(null, loadUser(), "Nota de teste com pedido nulo"));
    verify(orderHistoryRepository, times(0)).save(any());
  }

  @Test
  void given_create_when_userIsNull_then_throwBadRequestException() {
    assertThrows(BadRequestException.class, () -> orderHistoryService.create(loadOrder(), null, "Nota de teste com pedido nulo"));
    verify(orderHistoryRepository, times(0)).save(any());
  }

  @Test
  void given_findAllHistoryByOrderId_when_orderExists_then_returnPagedHistory() {
    Pageable pageable = PAGE_REQUEST;
    var order = loadOrder();
    List<OrderHistory> orderHistories = List.of(loadOrderHistory(order));
    Page<OrderHistory> orderHistoryPage = new PageImpl<>(orderHistories);

    when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
    when(orderHistoryRepository.findAllHistoryByOrderId(order.getId(), pageable)).thenReturn(orderHistoryPage);

    var result = orderHistoryService.findAllHistoryByOrderId(0, 10, "ACS", order.getId());

    assertEquals(1, result.getContent().size());
    verify(orderRepository, times(1)).findById(any());
    verify(orderHistoryRepository, times(1)).findAllHistoryByOrderId(order.getId(), pageable);
  }

  @Test
  void given_findAllHistoryByOrderId_when_orderNotExists_then_throwNotFoundException() {
    Pageable pageable = PAGE_REQUEST;
    var order = loadOrder();
    assertThrows(NotFoundException.class, () -> orderHistoryService.findAllHistoryByOrderId(0, 10, "ACS", order.getId()));

    verify(orderRepository, times(1)).findById(any());
    verify(orderHistoryRepository, times(0)).findAllHistoryByOrderId(order.getId(), pageable);
  }

  @Test
  void given_findAllHistory_then_returnPagedHistory() {
    Pageable pageable = PAGE_REQUEST;
    var order = loadOrder();
    List<OrderHistory> orderHistories = List.of(loadOrderHistory(order));
    Page<OrderHistory> orderHistoryPage = new PageImpl<>(orderHistories);

    when(orderHistoryRepository.findAll(pageable)).thenReturn(orderHistoryPage);

    var result = orderHistoryService.findAllHistory(0, 10, "ACS");

    assertEquals(1, result.getContent().size());
    verify(orderHistoryRepository, times(1)).findAll(pageable);
  }
}