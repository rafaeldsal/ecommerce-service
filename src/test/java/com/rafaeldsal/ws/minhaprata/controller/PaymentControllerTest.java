package com.rafaeldsal.ws.minhaprata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentInitResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.model.enums.PaymentStatus;
import com.rafaeldsal.ws.minhaprata.service.PaymentService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class PaymentControllerTest {

    private static final String USER_ID = IdGenerator.UUIDGenerator("user");
    private static final String ORDER_ID = IdGenerator.UUIDGenerator("order");
    private static final String ID = IdGenerator.UUIDGenerator("payment");
    private static final String TRANSACTION_ID = IdGenerator.UUIDGenerator("idempotence");
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2025, 4, 23, 19, 50);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void given_createPayment_when_successful_then_returnCreated() throws Exception {
        PaymentDto dto = PaymentDto.builder()
                .dtPayment(DATE_TIME)
                .paymentMethod("CARD")
                .amount(BigDecimal.valueOf(100.00))
                .savePaymentMethod(false)
                .currency("BRL")
                .orderId(ORDER_ID)
                .build();

        PaymentInitResponseDto responseDto = PaymentInitResponseDto.builder()
                .id(ID)
                .transactionId(TRANSACTION_ID)
                .status(PaymentStatus.REQUIRES_PAYMENT_METHOD.toString())
                .build();

        when(paymentService.create(dto)).thenReturn(responseDto);

        mockMvc.perform(post("/checkout/payment")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.transactionId").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.status").value(PaymentStatus.REQUIRES_PAYMENT_METHOD.toString()));
    }

    @Test
    void given_createPayment_when_paymentDtoMissingValues_then_returnBadRequest() throws Exception {
        PaymentDto dto = PaymentDto.builder().build();

        mockMvc.perform(post("/checkout/payment")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[amount=valor do pagamento é obrigatório, orderId=não pode ser nulo ou vazio, paymentMethod=é preciso informar o método de pagamento, currency=não poder nulo ou vazio]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));
    }


    @Test
    void given_findPaymentIntentStatus_then_returnPaymentStatus() throws Exception {
        PaymentUpdateStatus paymentUpdateStatus = PaymentUpdateStatus.builder()
                .paymentId(ID)
                .transactionId(TRANSACTION_ID)
                .paymentIntentId("pi_1N3mXYZabc1234567890")
                .status(PaymentStatus.SUCCEEDED.toString())
                .userId(USER_ID)
                .orderId(ORDER_ID)
                .clientSecret("cs_test_1234567890abcdef")
                .paymentErrorInfo(null)
                .build();

        when(paymentService.findPaymentIntentStatus(TRANSACTION_ID)).thenReturn(paymentUpdateStatus);

        mockMvc.perform(get("/checkout/payment/{transactionId}/status", TRANSACTION_ID))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId", Matchers.is(ID)))
                .andExpect(jsonPath("$.transactionId", Matchers.is(TRANSACTION_ID)))
                .andExpect(jsonPath("$.status", Matchers.is(PaymentStatus.SUCCEEDED.toString())))
                .andExpect(jsonPath("$.userId", Matchers.is(USER_ID)))
                .andExpect(jsonPath("$.orderId", Matchers.is(ORDER_ID)))
                .andExpect(jsonPath("$.paymentIntentId", Matchers.is("pi_1N3mXYZabc1234567890")));

    }

}