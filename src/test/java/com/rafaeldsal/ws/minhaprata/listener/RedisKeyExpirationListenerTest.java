package com.rafaeldsal.ws.minhaprata.listener;

import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderHistoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RedisKeyExpirationListenerTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderHistoryRepository orderHistoryRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RedisMessageListenerContainer listenerContainer;

  @InjectMocks
  private RedisKeyExpirationListener listener;

  @Test
  void testOnMessage_shouldExpireOrderAndRestoreStock() {
    var userId = IdGenerator.UUIDGenerator("user");
    var categoryId = IdGenerator.UUIDGenerator("cat");
    var orderId = IdGenerator.UUIDGenerator("order");
    var orderItemId = IdGenerator.UUIDGenerator("orderItem");
    var productId = IdGenerator.UUIDGenerator("prod");

    Product product = Product.builder()
        .id(productId)
        .name("Produto exemplo")
        .price(BigDecimal.valueOf(175.00))
        .stockQuantity(100L)
        .build();
    OrderItem orderItem = OrderItem.builder()
        .id(orderItemId)
        .quantity(2)
        .priceAtPurchase(BigDecimal.valueOf(175.00))
        .product(product)
        .build();

    User user = User.builder()
        .id(userId)
        .name("Rafael Souza")
        .email("rafael@email.com")
        .cpf("12345678900")
        .phoneNumber("61999999999")
        .dtBirth(LocalDate.of(2025, 4, 23))
        .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .role(UserRole.USER)
        .build();

    Order order = Order.builder()
        .id(IdGenerator.UUIDGenerator("order"))
        .orderItems(List.of(orderItem))
        .status(OrderStatus.PENDING)
        .totalPrice(BigDecimal.valueOf(350.00))
        .user(user)
        .build();

    // Mockito.when(orderRepository.findById(orderId).thenReturn(order);
    Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    String expiredKey = "order:123"; // A chave que expirou
    Message message = new DefaultMessage(expiredKey.getBytes(), new byte[0]);

    listener.onMessage(message, null);

    verify(orderRepository).save(argThat(order1 -> order1.getStatus() == OrderStatus.EXPIRED));
    verify(productRepository).save(argThat(product1 -> product1.getStockQuantity() == 102L));  // 100 + 2 (quantidade no pedido)
    verify(orderHistoryRepository).save(any());

  }
}