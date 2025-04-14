package com.rafaeldsal.ws.minhaprata.dto;

import com.rafaeldsal.ws.minhaprata.model.OrderStatus;

import java.util.List;

public record OrderHistoryDto(
  OrderStatus status,
  String note,
  List<OrderItemResponseDto> orderItemResponseDtos,
  Long userId,
  String name
) {
}
