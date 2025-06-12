package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.order.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<Page<OrderResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                        @RequestParam(value = "sort", defaultValue = "ASC") String sort,
                                                        @RequestParam(value = "userId", required = false) String userId,
                                                        @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus) {
    Page<OrderResponseDto> orderResponseDtos = orderService.findAll(page, size, sort, userId, orderStatus);
    return ResponseEntity.ok(orderResponseDtos);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(dto));
  }

  @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OrderResponseDto> update(@RequestParam("id") String orderId, @RequestParam("status")OrderStatus status) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.update(status, orderId));
  }
}
