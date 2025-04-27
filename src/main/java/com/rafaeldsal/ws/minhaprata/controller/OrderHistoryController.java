package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.orderHistory.OrderHistoryResponseDto;
import com.rafaeldsal.ws.minhaprata.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-history")
public class OrderHistoryController {

  @Autowired
  private OrderHistoryService orderHistoryService;

  @GetMapping
  public ResponseEntity<Page<OrderHistoryResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                               @RequestParam(value = "sort", defaultValue = "ASC") String sort) {
    Page<OrderHistoryResponseDto> orderHistoryResponseDtos = orderHistoryService.findAllHistory(page, size, sort);
    return ResponseEntity.ok(orderHistoryResponseDtos);
  }

  @GetMapping("{id}")
  public ResponseEntity<Page<OrderHistoryResponseDto>> findAllByOrderId(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                        @RequestParam(value = "sort", defaultValue = "ASC") String sort,
                                                                        @PathVariable("id") String orderId) {
    Page<OrderHistoryResponseDto> orderHistoryResponseDtos = orderHistoryService.findAllHistoryByOrderId(page, size, sort, orderId);
    return ResponseEntity.ok(orderHistoryResponseDtos);
  }
}
