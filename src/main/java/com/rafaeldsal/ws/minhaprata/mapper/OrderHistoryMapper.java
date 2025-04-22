package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.OrderHistoryResponseDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderHistory;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;

import java.time.LocalDateTime;

public class OrderHistoryMapper {

  public static OrderHistory toEntity(Order order, User user, String note) {
    return OrderHistory.builder()
        .status(order.getStatus())
        .dtEvent(LocalDateTime.now())
        .dtUpdated(LocalDateTime.now())
        .dtCreatedOrder(order.getDtOrder())
        .note(note)
        .order(order)
        .user(user)
        .build();
  }

  public static OrderHistoryResponseDto fromEntityToDto(OrderHistory orderHistory) {
    return OrderHistoryResponseDto.builder()
        .id(orderHistory.getId())
        .dtEvent(orderHistory.getDtEvent())
        .status(orderHistory.getStatus())
        .order(OrderMapper.entityToResponseDto(orderHistory.getOrder()))
        .userId(orderHistory.getUser().getId())
        .name(orderHistory.getUser().getName())
        .note(orderHistory.getNote())
        .role(orderHistory.getUser().getRole())
        .build();
  }
}
