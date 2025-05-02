package com.rafaeldsal.ws.minhaprata.mapper.address;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressViaCepDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Address;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;

public class AddressMapper {

  private AddressMapper(){}

  public static AddressResponseDto toResponseDto(Address address) {
    return AddressResponseDto.builder()
        .id(address.getId())
        .postalCode(address.getPostalCode())
        .city(address.getCity())
        .state(address.getState())
        .complement(address.getComplement())
        .neighborhood(address.getNeighborhood())
        .number(address.getNumber())
        .street(address.getStreet())
        .userId(address.getUser().getId())
        .build();
  }

  public static Address toEntity(AddressViaCepDto viaCep , AddressRequestDto dto, User user) {
    return Address.builder()
        .id(IdGenerator.UUIDGenerator("address"))
        .user(user)
        .state(viaCep.estado())
        .street(viaCep.logradouro())
        .postalCode(viaCep.cep())
        .neighborhood(viaCep.bairro())
        .number(dto.number())
        .complement(dto.complement())
        .city(viaCep.localidade())
        .build();
  }

  public static Address updateEntity(Address address, AddressRequestDto dto, AddressViaCepDto viaCep) {
    address.setCity(viaCep.localidade());
    address.setComplement(dto.complement());
    address.setNumber(dto.number());
    address.setPostalCode(viaCep.cep());
    address.setStreet(viaCep.logradouro());
    address.setState(viaCep.estado());
    address.setNeighborhood(viaCep.bairro());

    return address;
  }
}
