package com.rafaeldsal.ws.minhaprata.dto.orderHistory;

import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;

import java.util.List;

public record OrderHistoryDto(
  OrderStatus status,
  String note,
  List<OrderItemResponseDto> orderItemResponseDtos,
  String userId,
  String name
) {
}
