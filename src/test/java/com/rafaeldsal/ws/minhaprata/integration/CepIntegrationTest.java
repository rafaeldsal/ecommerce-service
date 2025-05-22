package com.rafaeldsal.ws.minhaprata.integration;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressViaCepDto;
import com.rafaeldsal.ws.minhaprata.integration.impl.CepIntegrationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CepIntegrationTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private CepIntegrationImpl cepIntegration;

  @Test
  void given_findCep_when_apiResponseIs201Find_then_returnAddressDto() {
    AddressRequestDto dto = AddressRequestDto.builder()
        .postalCode("01001000")
        .build();
    AddressViaCepDto response = AddressViaCepDto.builder()
        .cep("01001-000")
        .logradouro("Praça da Sé")
        .localidade("São Paulo")
        .estado("São Paulo")
        .bairro("Sé")
        .build();
    ReflectionTestUtils.setField(cepIntegration, "searchCepUrl", "https://viacep.com.br/ws/");
    when(restTemplate.exchange("https://viacep.com.br/ws/01001000/json/", HttpMethod.GET,null,AddressViaCepDto.class))
        .thenReturn(ResponseEntity.of(Optional.of(response)));

    assertEquals(response, cepIntegration.findCep(dto));
    verify(restTemplate, times(1)).exchange("https://viacep.com.br/ws/01001000/json/", HttpMethod.GET,null,AddressViaCepDto.class);
  }
}