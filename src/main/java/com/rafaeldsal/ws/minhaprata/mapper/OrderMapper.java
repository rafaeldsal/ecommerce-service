package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.OrderItemResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;

import java.util.List;

public class OrderMapper {

  public static OrderResponseDto entityToResponseDto(Order order) {
    return OrderResponseDto.builder()
        .id(order.getId())
        .dtOrder(order.getDtOrder())
        .dtUpdated(order.getDtUpdated())
        .status(order.getStatus())
        .totalPrice(order.getTotalPrice())
        .userId(order.getUser().getId())
        .itemResponseDto(mapOrderItems(order.getOrderItems()))
        .build();
  }

  private static List<OrderItemResponseDto> mapOrderItems(List<OrderItem> orderItems) {
    if (orderItems.isEmpty()) {
      return List.of();
    }

    return orderItems.stream()
        .map(orderItem -> OrderItemResponseDto.builder()
            .id(orderItem.getId())
            .productId(orderItem.getProduct().getId())
            .productName(orderItem.getProduct().getName())
            .priceAtPurchase(orderItem.getPriceAtPurchase())
            .quantity(orderItem.getQuantity())
            .build())
        .toList();
  }

  public static List<OrderResponseDto> entityListToResponseDtoList(List<Order> orders) {
    return orders.stream()
        .map(OrderMapper::entityToResponseDto)
        .toList();
  }
}
