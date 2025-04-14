package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.OrderHistoryResponseDto;
import org.springframework.data.domain.Page;

public interface OrderHistoryService {

  Page<OrderHistoryResponseDto> findAllHistoryByOrderId(Integer page, Integer size, String sort, Long orderId);

  Page<OrderHistoryResponseDto> findAllHistory(Integer page, Integer size, String sort);
}
