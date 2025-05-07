package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.IntegrationException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.CepIntegration;
import com.rafaeldsal.ws.minhaprata.mapper.address.AddressMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Address;
import com.rafaeldsal.ws.minhaprata.repository.jpa.AddressRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

  private final CepIntegration cepIntegration;
  private final AddressRepository addressRepository;
  private final UserRepository userRepository;

  @Override
  public AddressResponseDto create(AddressRequestDto addressDto) {

    if (Objects.nonNull(addressDto.id())) {
      throw new BadRequestException("addressId não pode ser definido");
    }

    var user = userRepository.findById(addressDto.userId())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    var cepValidByViaCep = cepIntegration.findCep(addressDto);

    if (cepValidByViaCep == null || Boolean.TRUE.equals(cepValidByViaCep.erro())) {
      throw new IntegrationException("CEP inválido ou não encontrado", HttpStatus.BAD_REQUEST);
    }

    Address address;

    if (user.getAddress() != null) {
      address = AddressMapper.updateEntity(user.getAddress(), addressDto, cepValidByViaCep);
    } else {
      address = AddressMapper.toEntity(cepValidByViaCep, addressDto, user);
    }

    addressRepository.save(address);
    return AddressMapper.toResponseDto(address);
  }

  @Override
  public AddressResponseDto findById(String userId) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    var address = user.getAddress();

    if (address == null) {
      throw new NotFoundException("Endereço não encontrado para o usuário");
    }

    return AddressMapper.toResponseDto(address);
  }
}
