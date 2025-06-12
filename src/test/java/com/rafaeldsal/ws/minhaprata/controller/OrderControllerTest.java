package com.rafaeldsal.ws.minhaprata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderDto;
import com.rafaeldsal.ws.minhaprata.dto.order.OrderResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemDto;
import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.service.OrderService;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class OrderControllerTest {

    private static final String USER_ID = IdGenerator.UUIDGenerator("user");
    private static final String CATEGORY_ID = IdGenerator.UUIDGenerator("cat");
    private static final String PRODUCT_ID = IdGenerator.UUIDGenerator("prod");
    private static final String ORDER_ITEM_ID = IdGenerator.UUIDGenerator("orderItem");
    private static final String ORDER_ID = IdGenerator.UUIDGenerator("order");
    private static final OrderStatus ORDER_STATUS = OrderStatus.PENDING;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

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

    @Test
    void given_findAll_then_returnAllOrders() throws Exception {
        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
        var order1 = loadOrderResponseDto();
        orderResponseDtos.add(order1);
        var order2 = loadOrderResponseDto();
        orderResponseDtos.add(order2);
        var order3 = loadOrderResponseDto();
        orderResponseDtos.add(order3);
        var order4 = loadOrderResponseDto();
        orderResponseDtos.add(order4);
        Page<OrderResponseDto> pagedOrders = new PageImpl<>(orderResponseDtos, PageRequest.of(0, 2), 5);

        when(orderService.findAll(0, 10, "ASC", null, null)).thenReturn(pagedOrders);

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "ASC"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).findAll(0, 4, "ASC", null, null);
    }

    @Test
    void given_create_when_orderDtoIsValid_then_returnOrderIsCreated() throws Exception {
        OrderDto dto = OrderDto.builder()
                .orderItems(List.of(OrderItemDto.builder()
                        .productId(PRODUCT_ID)
                        .quantity(2)
                        .priceAtPurchase(BigDecimal.valueOf(10))
                        .build()))
                .userId(USER_ID)
                .build();

        OrderResponseDto responseDto = loadOrderResponseDto();

        when(orderService.create(dto)).thenReturn(responseDto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(ORDER_ID)))
                .andExpect(jsonPath("$.status", Matchers.is(ORDER_STATUS.toString())));
    }

    @Test
    void given_create_when_orderDtoMissingValues_then_returnBadRequest() throws Exception {
        OrderDto dto = OrderDto.builder().build();

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[userId=não pode ser nulo]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));
    }

    @Test
    void given_update_when_orderDtoIsValid_then_returnOrderIsUpdated() throws Exception {
        OrderResponseDto responseDto = OrderResponseDto.builder()
                .id(ORDER_ID)
                .dtOrder(LocalDateTime.now())
                .itemResponseDto(List.of(loadOrderItemResponseDto()))
                .dtUpdated(LocalDateTime.now())
                .status(OrderStatus.IN_PROCESSING)
                .totalPrice(BigDecimal.valueOf(20))
                .userId(USER_ID)
                .build();

        when(orderService.update(OrderStatus.IN_PROCESSING, ORDER_ID)).thenReturn(responseDto);

        mockMvc.perform(patch("/orders")
                        .param("id", ORDER_ID)
                        .param("status", OrderStatus.IN_PROCESSING.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(ORDER_ID)))
                .andExpect(jsonPath("$.status", Matchers.is(OrderStatus.IN_PROCESSING.toString())));
    }
}