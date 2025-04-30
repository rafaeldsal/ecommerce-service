package com.rafaeldsal.ws.minhaprata.integration;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressViaCepDto;

public interface CepIntegration {

  AddressViaCepDto findCep(AddressRequestDto dto);
}
