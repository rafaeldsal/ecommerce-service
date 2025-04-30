package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressResponseDto;

public interface AddressService {

  AddressResponseDto create(AddressRequestDto addressDto);

  AddressResponseDto findById(String userId);

}
