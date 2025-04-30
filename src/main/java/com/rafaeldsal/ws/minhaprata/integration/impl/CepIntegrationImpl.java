package com.rafaeldsal.ws.minhaprata.integration.impl;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressViaCepDto;
import com.rafaeldsal.ws.minhaprata.integration.CepIntegration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class CepIntegrationImpl implements CepIntegration {

  @Value("${webservices.minhaprata.base.url.via.cep}")
  private String searchCepUrl;

  private final RestTemplate restTemplate;
  private final HttpHeaders headers;

  public CepIntegrationImpl() {
    this.restTemplate = new RestTemplate();
    headers = getHttpHeaders();
  }

  @Override
  public AddressViaCepDto findCep(AddressRequestDto dto) {
    try {
      HttpEntity<AddressRequestDto> request = new HttpEntity<>(dto, this.headers);
      ResponseEntity<AddressViaCepDto> response = restTemplate.exchange(
        searchCepUrl,
        HttpMethod.POST,
        request,
        AddressViaCepDto.class);
      return response.getBody();
    } catch (Exception e) {
      throw e;
    }
  }

  private HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    String credentials = "minhaprata:m1nh4pr4t4";
    String base64 = Base64.getEncoder().encodeToString(credentials.getBytes());
    headers.add("Authorization", "Basic " + base64);
    return headers;
  }
}
