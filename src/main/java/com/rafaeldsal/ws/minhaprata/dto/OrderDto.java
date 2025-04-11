package com.rafaeldsal.ws.minhaprata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderDto(

    @NotNull(message = "não pode ser nulo")
    Long userId,

    @Size(min = 1, message = "lista deve conter ao menos 1 pedido")
    List<OrderItemDto> orderItems

) {
}
