package com.rafaeldsal.ws.minhaprata.listener;

import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.OrderHistoryMapper;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderHistory;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderHistoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.OrderRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final OrderHistoryRepository orderHistoryRepository;

  public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer,
                                    OrderRepository orderRepository,
                                    ProductRepository productRepository,
                                    OrderHistoryRepository orderHistoryRepository) {
    super(listenerContainer);
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.orderHistoryRepository = orderHistoryRepository;
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String key = new String(message.getBody());

    if (key.startsWith("order:")) {
      Long orderId = Long.valueOf(key.replace("order:", ""));
      log.info("Processando expiração do pedido {}", orderId);

      orderRepository.findById(orderId).ifPresent(order -> {
        if (order.getStatus() != OrderStatus.PENDING) {
          log.info("Pedido {} já processado com status {}", orderId, order.getStatus());
          return;
        }

        // Restaurar estoque
        order.getOrderItems().forEach(orderItem -> {
          Product product = productRepository.findById(orderItem.getProduct().getId())
              .orElseThrow(() -> new NotFoundException("Produto não localizado"));

          product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
          productRepository.save(product);
        });

        // Atualizar status e salvar
        order.setStatus(OrderStatus.EXPIRED);
        order.setDtUpdated(LocalDateTime.now());
        orderRepository.save(order);

        // Histórico
        var user = order.getUser();
        OrderHistory orderHistory = OrderHistoryMapper.toEntity(order, user, "Pedido expirado automaticamente");
        orderHistoryRepository.save(orderHistory);
      });
    }
  }


}
