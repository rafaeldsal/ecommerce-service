package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(dto));
  }

  @PutMapping("{id}")
  public ResponseEntity<OrderResponseDto> update(@PathVariable("id") Long id, @RequestBody OrderDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.update(dto, id));
  }
}
