package com.rafaeldsal.ws.minhaprata.listener;

import com.rafaeldsal.ws.minhaprata.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

  private final Logger log = LoggerFactory.getLogger(RedisKeyExpirationListener.class);
  private final OrderService orderService;

  public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, OrderService orderService) {
    super(listenerContainer);
    this.orderService = orderService;
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      if (message == null || message.getBody() == null) {
        log.warn("Mensagem nula recebida ou corpo nulo");
        return;
      }

      String key = new String(message.getBody());
      if (key.startsWith("order:")) {
        String orderId = key.replace("order:", "");
        log.info("Processando expiração do pedido {}", orderId);
        orderService.handleExpiredOrder(orderId);
      }
    } catch (Exception e) {
      log.error("Erro ao processar mensagem expirada no Redis: {}", e);
    }
  }
}
