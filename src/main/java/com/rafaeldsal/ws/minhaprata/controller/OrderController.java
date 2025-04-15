package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<Page<OrderResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                        @RequestParam(value = "sort", defaultValue = "ASC") String sort) {
    Page<OrderResponseDto> orderResponseDtos = orderService.findAll(page, size, sort);
    return ResponseEntity.ok(orderResponseDtos);
  }

  @GetMapping("/users/{id}/orders")
  public ResponseEntity<Page<OrderResponseDto>> findAllByUserId(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                @RequestParam(value = "sort", defaultValue = "ASC") String sort,
                                                                @PathVariable("id") Long userId) {
    Page<OrderResponseDto> orderResponseDtos = orderService.findAllByUserId(page, size, sort, userId);
    return ResponseEntity.ok(orderResponseDtos);
  }

  @PostMapping
  public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(dto));
  }

  @PutMapping("cart/{id}")
  public ResponseEntity<OrderResponseDto> updateCartItems(@PathVariable("id") Long id, @RequestBody OrderDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.updateCartItems(dto, id));
  }

  @PatchMapping
  public ResponseEntity<OrderResponseDto> updateOrderStatus(@RequestParam("id") Long id, @RequestParam("status")OrderStatus status) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderStatus(status, id));
  }

}
