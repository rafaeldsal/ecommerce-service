package com.rafaeldsal.ws.minhaprata.integration.impl;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressViaCepDto;
import com.rafaeldsal.ws.minhaprata.exception.HttpClientException;
import com.rafaeldsal.ws.minhaprata.integration.CepIntegration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class CepIntegrationImpl implements CepIntegration {

  @Value("${webservices.minhaprata.base.url.via.cep}")
  private String searchCepUrl;

  private final RestTemplate restTemplate;

  public CepIntegrationImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public AddressViaCepDto findCep(AddressRequestDto dto) {
    try {
      String url = searchCepUrl + dto.postalCode() + "/json/";
      ResponseEntity<AddressViaCepDto> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        AddressViaCepDto.class);
      return response.getBody();
    } catch (Exception e) {
      throw new HttpClientException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
