package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.OrderItemResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.Order;

public class OrderMapper {

  public static OrderResponseDto entityToResponseDto(Order order) {
    return OrderResponseDto.builder()
        .id(order.getId())
        .status(order.getStatus())
        .itemResponseDto(order.getOrderItems().stream()
            .map(item -> OrderItemResponseDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .priceAtPurchase(item.getPriceAtPurchase())
                .quantity(item.getQuantity())
                .build())
            .toList())
        .dtOrder(order.getDtOrder())
        .dtUpdated(order.getDtUpdated())
        .totalPrice(order.getTotalPrice())
        .userId(order.getUser().getId())
        .build();
  }
}
