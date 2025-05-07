package com.rafaeldsal.ws.minhaprata.listener;

import com.rafaeldsal.ws.minhaprata.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

  private final OrderService orderService;

  public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, OrderService orderService) {
    super(listenerContainer);
    this.orderService = orderService;
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String key = new String(message.getBody());

    if (key.startsWith("order:")) {
      String orderId = key.replace("order:", "");
      log.info("Processando expiração do pedido {}", orderId);
      orderService.handleExpiredOrder(orderId);
    }
  }


}
