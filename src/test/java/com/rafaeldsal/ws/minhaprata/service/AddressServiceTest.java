package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressViaCepDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.IntegrationException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.CepIntegration;
import com.rafaeldsal.ws.minhaprata.mapper.address.AddressMapper;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.Address;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.repository.jpa.AddressRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.AddressServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

  private static final String ID = IdGenerator.UUIDGenerator("user");

  @Mock
  private AddressRepository addressRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CepIntegration cepIntegration;

  @InjectMocks
  private AddressServiceImpl addressService;

  private User loadUser() {
    return User.builder()
        .id(ID)
        .name("Rafael Souza")
        .email("rafael@email.com")
        .cpf("12345678900")
        .phoneNumber("61999999999")
        .dtBirth(LocalDate.of(2025, 4, 23))
        .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .role(UserRole.USER)
        .build();
  }


  @Test
  void given_create_when_addressIdIsProvided_then_throwBadRequestException() {
    AddressRequestDto dto = AddressRequestDto.builder()
        .id(IdGenerator.UUIDGenerator("address"))
        .build();

    Assertions.assertThrows(BadRequestException.class, () -> addressService.create(dto));

    verify(userRepository, times(0)).findById(any());
    verify(cepIntegration, times(0)).findCep(any());
    verify(addressRepository, times(0)).save(any());
  }

  @Test
  void given_create_when_userNotFound_then_throwNotFoundException() {
    AddressRequestDto dto = AddressRequestDto.builder().userId(ID).build();

    Assertions.assertThrows(NotFoundException.class, () -> addressService.create(dto));

    verify(userRepository, times(1)).findById(dto.userId());
    verify(cepIntegration, times(0)).findCep(any());
    verify(addressRepository, times(0)).save(any());
  }

  @Test
  void given_create_when_cepIsNull_then_throwIntegrationException() {
    AddressRequestDto dto = AddressRequestDto.builder()
        .userId(ID)
        .build();

    when(userRepository.findById(ID)).thenReturn(Optional.of(loadUser()));
    when(cepIntegration.findCep(dto)).thenReturn(null);

    Assertions.assertThrows(IntegrationException.class, () -> addressService.create(dto));

    verify(userRepository, times(1)).findById(dto.userId());
    verify(cepIntegration, times(1)).findCep(any());
    verify(addressRepository, times(0)).save(any());
  }

  @Test
  void given_create_when_cepResponseHasError_then_throwIntegrationException() {
    AddressRequestDto dto = AddressRequestDto.builder()
        .userId(ID)
        .build();

    AddressViaCepDto viaCepDto = AddressViaCepDto.builder()
        .erro(true)
        .build();

    when(userRepository.findById(ID)).thenReturn(Optional.of(loadUser()));
    when(cepIntegration.findCep(dto)).thenReturn(viaCepDto);

    Assertions.assertThrows(IntegrationException.class, () -> addressService.create(dto));

    verify(userRepository, times(1)).findById(dto.userId());
    verify(cepIntegration, times(1)).findCep(any());
    verify(addressRepository, times(0)).save(any());

  }

  @Test
  void given_create_when_userHasAddress_then_updateAndSaveAddress() {
    User user = loadUser();

    AddressRequestDto dto = AddressRequestDto.builder()
        .userId(user.getId())
        .number("05")
        .complement("lado ímpar")
        .postalCode("70070120")
        .build();

    Address address = new Address(
        IdGenerator.UUIDGenerator("address"), "Quadra SBS Quadra 2", "", "lado ímpar", "Asa Sul", "Brasília", "Distrito Federal", "70070120", user
    );

    user.setAddress(address);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(cepIntegration.findCep(any())).thenReturn(
        AddressViaCepDto.builder()
            .cep("70070120")
            .erro(false)
            .bairro("Asa Sul")
            .estado("Distrito Federal")
            .localidade("Brasília")
            .logradouro("Quadra SBS Quadra 2")
            .build());

    addressService.create(dto);

    verify(userRepository, times(1)).findById(ID);
    verify(cepIntegration, times(1)).findCep(any());
    verify(addressRepository, times(1)).save(address);
  }

  @Test
  void given_create_when_userHasNoAddress_then_createAndSaveAddress() {
    User user = loadUser();

    AddressRequestDto dto = AddressRequestDto.builder()
        .userId(user.getId())
        .number("05")
        .complement("lado ímpar")
        .postalCode("70070120")
        .build();

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(cepIntegration.findCep(any())).thenReturn(
        AddressViaCepDto.builder()
            .cep("70070120")
            .erro(false)
            .bairro("Asa Sul")
            .estado("Distrito Federal")
            .localidade("Brasília")
            .logradouro("Quadra SBS Quadra 2")
            .build());

    addressService.create(dto);

    verify(userRepository, times(1)).findById(user.getId());
    verify(cepIntegration, times(1)).findCep(any());
    verify(addressRepository, times(1)).save(any());
  }

  @Test
  void given_create_when_dataIsValid_then_returnMappedAddressResponse() {
    User user = loadUser();
    AddressRequestDto dto = AddressRequestDto.builder()
        .userId(user.getId())
        .number("05")
        .complement("lado ímpar")
        .postalCode("70070120")
        .build();
    AddressViaCepDto viaCepDto = AddressViaCepDto.builder()
        .cep("70070120")
        .erro(false)
        .bairro("Asa Sul")
        .estado("Distrito Federal")
        .localidade("Brasília")
        .logradouro("Quadra SBS Quadra 2")
        .build();
    Address address = AddressMapper.toEntity(viaCepDto, dto, user);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(cepIntegration.findCep(any())).thenReturn(viaCepDto);
    when(addressRepository.save(any())).thenReturn(address);

    AddressResponseDto response = addressService.create(dto);

    assertNotNull(response);
    assertEquals(address.getPostalCode(), response.postalCode());
    assertEquals(address.getStreet(), response.street());
    assertEquals(address.getNeighborhood(), response.neighborhood());
    assertEquals(address.getCity(), response.city());
    assertEquals(address.getComplement(), response.complement());
    assertEquals(address.getNumber(), response.number());
    assertEquals(address.getState(), response.state());

    verify(userRepository, times(1)).findById(user.getId());
    verify(cepIntegration, times(1)).findCep(dto);
    verify(addressRepository, times(1)).save(any());
  }

  @Test
  void given_findById_when_userNotFound_then_throwNotFoundException() {
    Assertions.assertThrows(NotFoundException.class, () -> addressService.findById(ID));

    verify(userRepository, times(1)).findById(ID);
  }

  @Test
  void given_findById_when_userHasNoAddress_then_throwNotFoundException() {
    User user = loadUser();

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    Assertions.assertThrows(NotFoundException.class, () -> addressService.findById(user.getId()));

    verify(userRepository, times(1)).findById(user.getId());
  }

  @Test
  void given_findById_when_userHasAddress_then_returnMappedAddressResponse() {
    User user = loadUser();
    Address address = new Address(
        IdGenerator.UUIDGenerator("address"),
        "Quadra SBS Quadra 2",
        "",
        "lado ímpar",
        "Asa Sul",
        "Brasília",
        "Distrito Federal",
        "70070120",
        user);
    user.setAddress(address);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    addressService.findById(user.getId());

    verify(userRepository, times(1)).findById(user.getId());
  }
}