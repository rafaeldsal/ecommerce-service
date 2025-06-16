package com.rafaeldsal.ws.minhaprata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.orderHistory.OrderHistoryResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.service.OrderHistoryService;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(OrderHistoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class OrderHistoryControllerTest {

    private static final String ORDER_HISTORY_ID = IdGenerator.UUIDGenerator("orderHistory");
    private static final String USER_ID = IdGenerator.UUIDGenerator("user");
    private static final String PRODUCT_ID = IdGenerator.UUIDGenerator("prod");
    private static final String ORDER_ITEM_ID = IdGenerator.UUIDGenerator("orderItem");
    private static final String ORDER_ID = IdGenerator.UUIDGenerator("order");
    private static final OrderStatus ORDER_STATUS = OrderStatus.PENDING;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderHistoryService orderHistoryService;

    @MockitoBean
    private TokenService tokenService;

    public OrderItemResponseDto loadOrderItemResponseDto() {
        return OrderItemResponseDto.builder()
                .id(ORDER_ITEM_ID)
                .priceAtPurchase(BigDecimal.valueOf(10))
                .productId(PRODUCT_ID)
                .productName("Produto Teste")
                .quantity(2)
                .build();
    }

    public OrderResponseDto loadOrderResponseDto() {
        return OrderResponseDto.builder()
                .id(ORDER_ID)
                .dtOrder(LocalDateTime.now())
                .itemResponseDto(List.of(loadOrderItemResponseDto()))
                .dtUpdated(LocalDateTime.now())
                .status(ORDER_STATUS)
                .totalPrice(BigDecimal.valueOf(20))
                .userId(USER_ID)
                .build();
    }

    public OrderHistoryResponseDto loadOrderHistoryResponseDto() {
        return OrderHistoryResponseDto.builder()
                .id(ORDER_HISTORY_ID)
                .dtEvent(LocalDateTime.now())
                .dtCreated(LocalDateTime.now())
                .status(ORDER_STATUS)
                .name("Usuário")
                .role(UserRole.USER)
                .note("Nota de teste")
                .order(loadOrderResponseDto())
                .userId(USER_ID)
                .build();
    }

    @Test
    void given_findAll_then_returnAllOrderHistory() throws Exception {
        List<OrderHistoryResponseDto> orderHistoryResponseDtos = new ArrayList<>();
        var orderHistory1 = loadOrderHistoryResponseDto();
        orderHistoryResponseDtos.add(orderHistory1);
        var orderHistory2 = loadOrderHistoryResponseDto();
        orderHistoryResponseDtos.add(orderHistory2);
        var orderHistory3 = loadOrderHistoryResponseDto();
        orderHistoryResponseDtos.add(orderHistory3);
        Page<OrderHistoryResponseDto> pagedOrderHistory = new PageImpl<>(orderHistoryResponseDtos, PageRequest.of(0, 10), 5);

        when(orderHistoryService.findAllHistory(0, 10, "ASC")).thenReturn(pagedOrderHistory);

        mockMvc.perform(get("/order-history")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "ASC"))
                .andExpect(status().isOk());

        verify(orderHistoryService, times(1)).findAllHistory(0, 10, "ASC");
    }

    @Test
    void given_findAllByOrderId_then_returnAllOrderHistory() throws Exception {
        List<OrderHistoryResponseDto> orderHistoryResponseDtos = new ArrayList<>();
        var orderHistory1 = loadOrderHistoryResponseDto();
        orderHistoryResponseDtos.add(orderHistory1);
        Page<OrderHistoryResponseDto> pagedOrderHistory = new PageImpl<>(orderHistoryResponseDtos, PageRequest.of(0, 10), 5);

        when(orderHistoryService.findAllHistoryByOrderId(0, 10, "ASC", ORDER_ID)).thenReturn(pagedOrderHistory);

        mockMvc.perform(get("/order-history/{id}", ORDER_ID)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", Matchers.is(ORDER_HISTORY_ID)));

        verify(orderHistoryService, times(1)).findAllHistoryByOrderId(0, 10, "ASC", ORDER_ID);
    }
}