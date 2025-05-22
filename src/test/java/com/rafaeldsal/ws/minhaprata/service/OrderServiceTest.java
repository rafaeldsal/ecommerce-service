package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.order.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemDto;
import com.rafaeldsal.ws.minhaprata.exception.BusinessException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.orderItem.OrderItemMapper;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.model.redis.PendingOrder;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.OrderHistoryServiceImpl;
import com.rafaeldsal.ws.minhaprata.service.impl.OrderServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10, Sort.by(SortUtils.getSortDirection("ASC"), "dtOrder"));
  private static final String USER_ID = IdGenerator.UUIDGenerator("user");
  private static final String CATEGORY_ID = IdGenerator.UUIDGenerator("cat");
  private static final String PRODUCT_ID = IdGenerator.UUIDGenerator("prod");
  private static final String ORDER_ITEM_ID = IdGenerator.UUIDGenerator("orderItem");
  private static final String ORDER_ID = IdGenerator.UUIDGenerator("order");
  private static final OrderStatus ORDER_STATUS = OrderStatus.PENDING;

  @Mock
  private UserRepository userRepository;
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private OrderItemMapper orderItemMapper;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private RedisTemplate<String, Object> redisTemplate;
  @Mock
  private ValueOperations<String, Object> valueOperations;
  @Mock
  private OrderHistoryServiceImpl orderHistoryService;

  @InjectMocks
  private OrderServiceImpl orderService;

  @Test
  void given_findById_when_thereIsOrder_then_returnOrderResponseDto() {
    Order order = getOrder();
    when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

    orderService.findById(order.getId());

    verify(orderRepository, times(1)).findById(any());
  }

  @Test
  void given_findById_when_thereIsNotOrder_then_throwNotFoundException() {
    when(orderRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> orderService.findById("invalidId"));

    verify(orderRepository, times(1)).findById(any());
  }

  @Test
  void given_findAll_when_noExistsFilters_then_returnAllOrdersPaged() {
    Pageable pageable = PAGE_REQUEST;
    List<Order> orders = List.of(getOrder());
    Page<Order> orderPage = new PageImpl<>(orders);

    when(orderRepository.findAll(pageable)).thenReturn(orderPage);

    var result = orderService.findAll(0, 10, "UNSORTED", null, null);

    assertEquals(1, result.getContent().size());
    verify(orderRepository, times(1)).findAll(pageable);
  }

  @Test
  void given_findAll_when_userIdAndStatusIsNotNull_then_returnAllOrdersPaged() {
    Pageable pageable = PAGE_REQUEST;
    List<Order> orders = List.of(getOrder());
    Page<Order> orderPage = new PageImpl<>(orders);

    when(orderRepository.findAllByUserIdAndStatus(USER_ID, ORDER_STATUS, pageable)).thenReturn(orderPage);

    var result = orderService.findAll(0, 10, "UNSORTED", USER_ID, ORDER_STATUS);

    assertEquals(1, result.getContent().size());
    verify(orderRepository, times(1)).findAllByUserIdAndStatus(USER_ID, ORDER_STATUS, pageable);
  }

  @Test
  void given_findAll_when_onlyUserIdIsNotNull_then_returnAllOrdersPaged() {
    Pageable pageable = PAGE_REQUEST;
    List<Order> orders = List.of(getOrder());
    Page<Order> orderPage = new PageImpl<>(orders);

    when(orderRepository.findAllByUserId(USER_ID, pageable)).thenReturn(orderPage);

    var result = orderService.findAll(0, 10, "UNSORTED", USER_ID, null);

    assertEquals(1, result.getContent().size());
    verify(orderRepository, times(1)).findAllByUserId(USER_ID, pageable);
  }

  @Test
  void given_findAll_when_onlyOrderStatusIsNotNull_then_returnAllOrdersPaged() {
    Pageable pageable = PAGE_REQUEST;
    List<Order> orders = List.of(getOrder());
    Page<Order> orderPage = new PageImpl<>(orders);

    when(orderRepository.findAllByStatus(ORDER_STATUS, pageable)).thenReturn(orderPage);

    var result = orderService.findAll(0, 10, "UNSORTED", null, ORDER_STATUS);

    assertEquals(1, result.getContent().size());
    verify(orderRepository, times(1)).findAllByStatus(ORDER_STATUS, pageable);
  }

  @Test
  void given_create_when_validOrderDto_then_saveOrderAndPersistInRedis() {
    OrderDto dto = getOrderDto();

    when(userRepository.findById(dto.userId())).thenReturn(Optional.of(getUser()));
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(getProduct()));
    when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    when(orderRepository.save(any(Order.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    doNothing().when(valueOperations).set(anyString(), any(PendingOrder.class), any(Duration.class));
    when(orderItemMapper.toEntity(any(Order.class), any(OrderItemDto.class), any(Product.class))).thenReturn(getOrderItem());

    OrderResponseDto response = orderService.create(dto);

    assertNotNull(response);
    assertEquals(1, response.itemResponseDto().size());

    verify(userRepository, times(1)).findById(dto.userId());
    verify(productRepository, times(1)).findById(PRODUCT_ID);
    verify(productRepository, times(1)).save(any(Product.class));
    verify(orderRepository, times(1)).save(any(Order.class));
    verify(redisTemplate).opsForValue();
    verify(valueOperations).set(
        startsWith("order:"),
        any(PendingOrder.class),
        eq(Duration.ofSeconds(60))
    );
    verify(orderHistoryService, times(1)).create(any(Order.class), any(User.class), eq("Pedido criado com sucesso"));
  }

  @Test
  void given_create_when_userIsNotFound_then_throwsNotFoundException() {
    OrderDto dto = getOrderDto();
    when(userRepository.findById(dto.userId())).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> orderService.create(dto));
    verify(userRepository, times(1)).findById(dto.userId());
  }

  @Test
  void given_create_when_productIsNotFound_then_throwsNotFoundException() {
    OrderDto dto = getOrderDto();

    when(userRepository.findById(dto.userId())).thenReturn(Optional.of(getUser()));
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> orderService.create(dto));

    verify(userRepository, times(1)).findById(dto.userId());
    verify(productRepository, times(1)).findById(PRODUCT_ID);
  }

  @Test
  void given_create_when_productWithInsufficientStock_then_throwBusinessException() {
    OrderDto dto = getOrderDto();
    Product mockProduct = mock(Product.class);
    when(mockProduct.getId()).thenReturn(PRODUCT_ID);

    when(userRepository.findById(dto.userId())).thenReturn(Optional.of(getUser()));
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(mockProduct));

    doThrow(new BusinessException("Estoque insuficiente"))
        .when(mockProduct)
        .decreaseStockOrFail(anyLong());

    assertThrows(BusinessException.class, () -> orderService.create(dto));

    verify(userRepository, times(1)).findById(dto.userId());
    verify(productRepository, times(1)).findById(mockProduct.getId());
    verify(mockProduct).decreaseStockOrFail(1);
  }

  @Test
  void given_create_when_quantityLessThanZero_then_throwsBusinessException() {
    OrderDto dto = OrderDto.builder()
        .orderItems(List.of(
            OrderItemDto.builder()
                .productId(PRODUCT_ID)
                .quantity(0)
                .build()
        ))
        .userId(USER_ID)
        .build();
    Product product = mock(Product.class);

    when(userRepository.findById(dto.userId())).thenReturn(Optional.of(getUser()));
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

    doThrow(new BusinessException("Quantidade inválida"))
        .when(product)
        .decreaseStockOrFail(0L);

    assertThrows(BusinessException.class, () -> orderService.create(dto));

    verify(userRepository, times(1)).findById(dto.userId());
    verify(productRepository, times(1)).findById(PRODUCT_ID);
    verify(product).decreaseStockOrFail(0);
  }

  @Test
  void given_handleExpiredOrder_when_expiredOrderWithPendingStatus_then_expireOrderAndRestoreStock() {
    Order order = getOrder();
    OrderItem orderItem = getOrderItem();
    order.setOrderItems(List.of(orderItem));
    when(orderRepository.findByIdWithOrderItems(ORDER_ID)).thenReturn(Optional.of(order));

    List<OrderItem> orderItems = List.of(orderItem);
    List<Product> products = List.of(getProduct());
    List<String> productsIds = orderItems.stream()
        .map(item -> item.getProduct().getId())
        .toList();

    when(productRepository.findAllByForUpdate(productsIds)).thenReturn(products);
    when(productRepository.saveAllAndFlush(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    orderService.handleExpiredOrder(ORDER_ID);

    verify(orderRepository, times(1)).findByIdWithOrderItems(ORDER_ID);
    verify(orderRepository, times(1)).save(any(Order.class));
    verify(productRepository, times(1)).findAllByForUpdate(productsIds);
    verify(productRepository, times(1)).saveAllAndFlush(any());
    verify(orderHistoryService, times(1)).create(any(Order.class), any(User.class), eq("Pedido expirado por timeout de pagamento"));
  }

  @Test
  void given_handlerExpiredOrder_when_orderIsNotPendingStatus_then_returnsEmpty() {
    Order order = getOrder();
    order.setStatus(OrderStatus.PAID);
    when(orderRepository.findByIdWithOrderItems(ORDER_ID)).thenReturn(Optional.of(order));

    orderService.handleExpiredOrder(ORDER_ID);

    verify(orderRepository, times(1)).findByIdWithOrderItems(ORDER_ID);
    verify(orderRepository, times(0)).save(any(Order.class));
    verify(orderHistoryService, times(0)).create(any(Order.class), any(User.class), eq("Pedido expirado por timeout de pagamento"));
  }

  @Test
  void given_handleExpiredOrder_when_orderIsNotFound_then_throwsNotFoundException() {
    when(orderRepository.findByIdWithOrderItems(ORDER_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> orderService.handleExpiredOrder(ORDER_ID));

    verify(orderRepository, times(1)).findByIdWithOrderItems(ORDER_ID);
    verify(orderHistoryService, times(0)).create(any(Order.class), any(User.class), eq("Pedido expirado por timeout de pagamento"));
  }

  @Test
  void given_update_when_validUpdateToDifferentStatus_then_changeStatusAndHandleStock() {
    Order order = getOrder();

    when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    OrderResponseDto response = orderService.update(OrderStatus.IN_PROCESSING, ORDER_ID);

    assertNotNull(response);

    verify(orderRepository, times(1)).findById(ORDER_ID);
    verify(orderRepository, times(1)).save(any(Order.class));
    verify(orderHistoryService, times(1)).create(any(Order.class), any(User.class), eq("Atualizando pedido pela tela de ADM"));
  }

  @Test
  void given_update_when_updateWithSameStatus_then_throwsBusinessException() {
    Order order = getOrder();
    order.setStatus(OrderStatus.IN_PROCESSING);

    when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

    assertEquals("Pedido já se encontra nesse status " + OrderStatus.IN_PROCESSING,
        assertThrows(BusinessException.class, () -> orderService.update(OrderStatus.IN_PROCESSING, ORDER_ID)).getLocalizedMessage());

    verify(orderRepository, times(1)).findById(ORDER_ID);
  }

  @Test
  void given_update_when_orderWithFinalStatus_then_throwsBusinessException() {
    Order order = getOrder();
    order.setStatus(OrderStatus.PAID);

    when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

    assertEquals("Pedido não pode ser cancelado.",
        assertThrows(BusinessException.class, () -> orderService.update(OrderStatus.CANCELLED, ORDER_ID)).getLocalizedMessage());

    verify(orderRepository, times(1)).findById(ORDER_ID);
  }

  @Test
  void given_updateStatusFromWebhook_when_validWebhookStatusChange_then_updateOrderStatus() {
    Order order = getOrder();
    when(orderRepository.save(any(Order.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    orderService.updateStatusFromWebhook(order, OrderStatus.IN_PROCESSING);

    verify(orderRepository, times(1)).save(any(Order.class));
    verify(orderHistoryService, times(1)).create(any(Order.class), any(User.class), eq("Pedido com status alterado para " + OrderStatus.IN_PROCESSING + " via WebHook Stripe"));
  }

  @Test
  void given_updateStatusFromWebhook_when_redundantWebhookStatus_then_throwsBusinessException() {
    Order order = getOrder();
    order.setStatus(OrderStatus.IN_PROCESSING);

    assertEquals("Pedido já se encontra nesse status " + OrderStatus.IN_PROCESSING,
        assertThrows(BusinessException.class, () -> orderService.updateStatusFromWebhook(order, OrderStatus.IN_PROCESSING))
            .getLocalizedMessage());

    verify(orderRepository, times(0)).save(any(Order.class));
    verify(orderHistoryService, times(0)).create(any(Order.class), any(User.class), eq("Pedido com status alterado para " + OrderStatus.IN_PROCESSING + " via WebHook Stripe"));
  }

  @Test
  void given_updateStatusFromWebhook_when_updateToCancelledFromWebhook_then_throwsBusinessException() {
    Order order = getOrder();
    order.setStatus(OrderStatus.PAID);

    assertEquals("Pedido não pode ser cancelado.",
        assertThrows(BusinessException.class, () -> orderService.updateStatusFromWebhook(order, OrderStatus.CANCELLED))
            .getLocalizedMessage());

    verify(orderRepository, times(0)).save(any(Order.class));
    verify(orderHistoryService, times(0)).create(any(Order.class), any(User.class), eq("Pedido com status alterado para " + OrderStatus.IN_PROCESSING + " via WebHook Stripe"));
  }

  private OrderDto getOrderDto() {
    return OrderDto.builder()
        .orderItems(List.of(getOrderItemDto()))
        .userId(USER_ID)
        .build();
  }

  private OrderItemDto getOrderItemDto() {
    return OrderItemDto.builder()
        .priceAtPurchase(BigDecimal.valueOf(250.0))
        .productId(PRODUCT_ID)
        .quantity(1)
        .build();
  }

  private OrderItem getOrderItem() {
    return OrderItem.builder()
        .product(getProduct())
        .id(ORDER_ITEM_ID)
        .order(getOrder())
        .priceAtPurchase(BigDecimal.valueOf(250.0))
        .quantity(1)
        .build();
  }

  private User getUser() {
    return  User.builder()
        .id(USER_ID)
        .name("Rafael Souza")
        .email("rafael@email.com")
        .cpf("12345678900")
        .phoneNumber("61999999999")
        .dtBirth(LocalDate.of(2025, 4, 23))
        .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .role(UserRole.USER).build();
  }

  private Order getOrder() {
    return Order.builder()
        .id(ORDER_ID)
        .orderItems(new ArrayList<>())
        .dtOrder(LocalDateTime.now())
        .dtUpdated(LocalDateTime.now())
        .user(getUser())
        .status(ORDER_STATUS)
        .totalPrice(BigDecimal.valueOf(500.00))
        .payment(null)
        .build();
  }

  private Category category() {
    return Category.builder()
        .description("Categoria de teste")
        .id(CATEGORY_ID)
        .name("Categoria Teste")
        .build();
  }

  private Product getProduct() {
    return Product.builder()
        .id(PRODUCT_ID)
        .name("Produto teste")
        .description("Produto criado para teste unitário")
        .imgUrl("http://imagem-teste.com.br")
        .price(BigDecimal.valueOf(250.0))
        .category(category())
        .stockQuantity(100L)
        .dtCreated(LocalDateTime.now())
        .dtUpdated(LocalDateTime.now())
        .build();
  }

}