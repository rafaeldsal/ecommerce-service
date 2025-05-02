package com.rafaeldsal.ws.minhaprata.dto.order;

import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderDto(
    @NotNull(message = "não pode ser nulo")
    String userId,
    @Size(min = 1, message = "lista deve conter ao menos 1 pedido")
    List<OrderItemDto> orderItems
) {
}
