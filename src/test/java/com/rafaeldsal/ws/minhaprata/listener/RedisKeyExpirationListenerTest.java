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
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisKeyExpirationListenerTest {

  @Mock
  private OrderService orderService;

  @Mock
  private Logger mockLogger;

  @InjectMocks
  private RedisKeyExpirationListener listener;

  @Mock
  private RedisMessageListenerContainer listenerContainer;

  private final byte[] PATTERN = "__keyevent@0__:expired".getBytes();

  @Test
  void testOnMessage_shouldExpireOrderAndRestoreStock() {
    var orderId = IdGenerator.UUIDGenerator("order");

    String expiredKey = "order:" + orderId;

    Message message = new DefaultMessage(PATTERN, expiredKey.getBytes());

    listener.onMessage(message, PATTERN);

    verify(orderService).handleExpiredOrder(orderId);
  }

  @Test
  void testOnMessage_shouldIgnoreNonOrderKeys() {
    String nonOrderKey = "user:123";

    Message message = new DefaultMessage(PATTERN, nonOrderKey.getBytes());

    listener.onMessage(message, PATTERN);

    verify(orderService, never()).handleExpiredOrder(any());
  }

  @Test
  void testOnMessage_shouldHandleNullMessage() {
    listener.onMessage(null, PATTERN);

    verify(orderService, never()).handleExpiredOrder(any());
  }

  @Test
  void testOnMessage_shouldHandleNullBody() {
    Message message = mock(Message.class);
    when(message.getBody()).thenReturn(null);

    listener.onMessage(message, PATTERN);

    verify(orderService, never()).handleExpiredOrder(any());
  }

  @Test
  void testOnMessage_shouldHandleServiceException() throws Exception {
    String orderId = IdGenerator.UUIDGenerator("order");
    String orderKey = "order:" + orderId;
    Message message = new DefaultMessage(PATTERN, orderKey.getBytes());

    doThrow(new RuntimeException("Service error")).when(orderService).handleExpiredOrder(orderId);

    listener.onMessage(message, PATTERN);

    verify(mockLogger).error(eq("Erro ao processar mensagem expirada no Redis: {}"), any(RuntimeException.class));

  }
}