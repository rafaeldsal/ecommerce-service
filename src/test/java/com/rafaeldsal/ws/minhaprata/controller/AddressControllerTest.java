package com.rafaeldsal.ws.minhaprata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressResponseDto;
import com.rafaeldsal.ws.minhaprata.service.AddressService;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class AddressControllerTest {

    private final String USER_ID = IdGenerator.UUIDGenerator("user");
    private final String ADDRESS_ID = IdGenerator.UUIDGenerator("address");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AddressService addressService;

    @Test
    void given_create_when_addressRequestDtoIsValid_then_returnIsCreated() throws Exception {
        AddressRequestDto request = AddressRequestDto.builder()
                .id(null)
                .userId(USER_ID)
                .postalCode("70070120")
                .complement("lado ímpar")
                .number("")
                .build();

        AddressResponseDto response = AddressResponseDto.builder()
                .city("Brasília")
                .state("Distrito Federal")
                .neighborhood("Asa Sul")
                .street("Quadra SBS Quadra 2")
                .userId(USER_ID)
                .complement("lado ímpar")
                .id(ADDRESS_ID)
                .number("")
                .postalCode("70070120")
                .build();

        when(addressService.create(request)).thenReturn(response);

        mockMvc.perform(post("/checkout/address")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(ADDRESS_ID)))
                .andExpect(jsonPath("$.postalCode", Matchers.is("70070120")));

        verify(addressService, times(1)).create(request);
    }

    @Test
    void given_create_when_addressRequestDtoIsMissingValues_then_returnBadRequest() throws Exception {
        AddressRequestDto request = AddressRequestDto.builder()
                .id(null)
                .userId(" ")
                .postalCode("700")
                .build();

        mockMvc.perform(post("/checkout/address")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[postalCode=CEP inválido. Deve conter exatamente 8 dígitos numéricos, userId=não pode ser nulo ou vazio]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

        verify(addressService, times(0)).create(any());
    }

    @Test
    void given_findById_when_idIsValid_then_returnAddressResponseDtoIsOk() throws Exception {
        AddressResponseDto response = AddressResponseDto.builder()
                .city("Brasília")
                .state("Distrito Federal")
                .neighborhood("Asa Sul")
                .street("Quadra SBS Quadra 2")
                .userId(USER_ID)
                .complement("lado ímpar")
                .id(ADDRESS_ID)
                .number("")
                .postalCode("70070120")
                .build();

        when(addressService.findById(USER_ID)).thenReturn(response);

        mockMvc.perform(get("/checkout/address/{userId}", USER_ID))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(ADDRESS_ID)))
                .andExpect(jsonPath("$.userId", Matchers.is(USER_ID)))
                .andExpect(jsonPath("$.street", Matchers.is("Quadra SBS Quadra 2")))
                .andExpect(jsonPath("$.complement", Matchers.is("lado ímpar")))
                .andExpect(jsonPath("$.postalCode", Matchers.is("70070120")));
    }

}