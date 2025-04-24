package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.MailIntegration;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserDetailsRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.UserServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final long ID = 1L;
  public static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10, Sort.by(SortUtils.getSortDirection("ASC"), "name"));

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserDetailsRepository userDetailsRepository;

  @Mock
  private MailIntegration mailIntegration;

  @InjectMocks
  private UserServiceImpl userService;

  private UserDto userDto() {
    return UserDto.builder()
        .name("Rafael Alves")
        .email("rafael@email.com")
        .cpf("12345678900")
        .phoneNumber("61999999999")
        .dtBirth(LocalDate.of(2025, 4, 23))
        .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50))
        .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50))
        .password("senha123")
        .build();
  }

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

  private UserResponseDto loadUserResponseDto(User user) {
    return UserResponseDto.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .cpf(user.getCpf())
        .phoneNumber(user.getPhoneNumber())
        .dtBirth(user.getDtBirth())
        .dtCreated(user.getDtCreated())
        .dtUpdated(user.getDtUpdated())
        .build();
  }

  @Test
  void given_validPageRequestWithoutName_when_findAll_then_returnPagedUsers() {
    Pageable pageable = PAGE_REQUEST;
    List<User> users = List.of(loadUser());
    Page<User> userPage = new PageImpl<>(users);

    when(userRepository.findAll(pageable)).thenReturn(userPage);

    Page<UserResponseDto> result = userService.findAll(0, 10, "UNSORTED", null);

    Assertions.assertEquals(1, result.getContent().size());
    verify(userRepository, times(1)).findAll(pageable);
  }

  @Test
  void given_validPageRequestWithName_when_findAll_then_returnFilteredPagedUsers() {
    Pageable pageable = PAGE_REQUEST;
    List<User> users = List.of(loadUser());
    Page<User> userPage = new PageImpl<>(users);

    when(userRepository.findByNameContainingIgnoreCase("Rafael", pageable)).thenReturn(userPage);

    var result = userService.findAll(0, 10, "ASC", "Rafael");

    Assertions.assertEquals(1, result.getContent().size());
    verify(userRepository, times(1)).findByNameContainingIgnoreCase("Rafael", pageable);
  }

  @Test
  void given_blankName_when_findAll_then_returnAllPagedUsers() {
    Pageable pageable = PAGE_REQUEST;
    List<User> users = List.of(loadUser());
    Page<User> userPage = new PageImpl<>(users);

    when(userRepository.findAll(pageable)).thenReturn(userPage);

    Page<UserResponseDto> result = userService.findAll(0, 10, "UNSORTED", "");

    Assertions.assertEquals(1, result.getContent().size());
    verify(userRepository, times(1)).findAll(pageable);
  }

  @Test
  void given_noUsersInDatabase_when_findAll_then_returnEmptyPage() {
    Pageable pageable = PAGE_REQUEST;
    when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

    Page<UserResponseDto> result = userService.findAll(0, 10, "UNSORTED", "");

    Assertions.assertEquals(0, result.getContent().size());
    verify(userRepository, times(1)).findAll(pageable);
  }

  @Test
  void given_findById_when_userExists_then_returnUserResponseDto() {
    var user = loadUser();
    UserResponseDto dto = loadUserResponseDto(user);
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    Assertions.assertEquals(dto, userService.findByID(ID));

    verify(userRepository, times(1)).findById(ID);
  }

  @Test
  void given_findById_when_userIsNotExists_then_throwNotFoundException() {
    when(userRepository.findById(ID)).thenReturn(Optional.empty());
    Assertions.assertThrows(NotFoundException.class, () -> userService.findByID(ID));
    verify(userRepository, times(1)).findById(ID);
  }

  @Test
  void given_validUserDto_when_create_then_returnCreatedUserResponseDto() {
    UserDto dto = userDto();
    when(userRepository.existsByCpf(dto.cpf())).thenReturn(false);
    when(userRepository.existsByEmail(dto.email())).thenReturn(false);
    when(userRepository.existsByPhoneNumber(dto.phoneNumber())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    when(userDetailsRepository.save(any(UserCredentials.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserResponseDto userResponseDto = userService.create(dto);

    Assertions.assertEquals(dto.name(), userResponseDto.name());
    Assertions.assertEquals(dto.email(), userResponseDto.email());
    Assertions.assertEquals(dto.phoneNumber(), userResponseDto.phoneNumber());
    Assertions.assertEquals(dto.dtBirth(), userResponseDto.dtBirth());
    Assertions.assertEquals(dto.dtCreated(), userResponseDto.dtCreated());
    Assertions.assertEquals(dto.dtUpdated(), userResponseDto.dtUpdated());
    Assertions.assertEquals(dto.cpf(), userResponseDto.cpf());

    verify(userRepository, times(1)).existsByEmail(any());
    verify(userRepository, times(1)).existsByPhoneNumber(any());
    verify(userRepository, times(1)).existsByCpf(any());

    verify(userRepository, times(1)).save(any());
    verify(mailIntegration, times(1)).send(eq(dto.email()), anyString(), eq("\u2728 Seja bem-vindo ao Minha Prata!"));
    verify(userDetailsRepository, times(1)).save(any());
  }

  @Test
  void given_userDtoWithId_when_create_then_throwBadRequestException() {
    UserDto dto = UserDto.builder().id(ID).build();
    Assertions.assertThrows(BadRequestException.class, () -> userService.create(dto));

    verify(userRepository, times(0)).existsByEmail(any());
    verify(userRepository, times(0)).existsByPhoneNumber(any());
    verify(userRepository, times(0)).existsByCpf(any());
    verify(userRepository, times(0)).save(any());
    verify(userDetailsRepository, times(0)).save(any());
  }

  @Test
  void given_userDtoWithExistingCpf_when_create_then_throwBadRequestException() {
    UserDto dto = UserDto.builder()
        .cpf("12345678901")
        .build();

    when(userRepository.existsByCpf(dto.cpf())).thenReturn(true);

    Assertions.assertThrows(BadRequestException.class, () -> userService.create(dto));

    verify(userRepository, times(0)).existsByEmail(any());
    verify(userRepository, times(0)).existsByPhoneNumber(any());
    verify(userRepository, times(1)).existsByCpf(any());
    verify(userRepository, times(0)).save(any());
    verify(userDetailsRepository, times(0)).save(any());
  }

  @Test
  void given_userDtoWithExistingEmail_when_create_then_throwBadRequestException() {
    UserDto dto = UserDto.builder()
        .cpf("12345678901")
        .email("rafael@mail.com")
        .build();

    when(userRepository.existsByEmail(dto.email())).thenReturn(true);

    Assertions.assertThrows(BadRequestException.class, () -> userService.create(dto));

    verify(userRepository, times(1)).existsByEmail(any());
    verify(userRepository, times(0)).existsByPhoneNumber(any());
    verify(userRepository, times(1)).existsByCpf(any());
    verify(userRepository, times(0)).save(any());
    verify(userDetailsRepository, times(0)).save(any());
  }

  @Test
  void given_userDtoWithExistingPhone_when_create_then_throwBadRequestException() {
    UserDto dto = UserDto.builder()
        .cpf("12345678901")
        .email("rafael@mail.com")
        .phoneNumber("61999999999")
        .build();

    when(userRepository.existsByPhoneNumber(dto.phoneNumber())).thenReturn(true);

    Assertions.assertThrows(BadRequestException.class, () -> userService.create(dto));

    verify(userRepository, times(1)).existsByEmail(any());
    verify(userRepository, times(1)).existsByPhoneNumber(any());
    verify(userRepository, times(1)).existsByCpf(any());
    verify(userRepository, times(0)).save(any());
    verify(userDetailsRepository, times(0)).save(any());
  }

  @Test
  void given_validUserDto_when_update_then_returnUpdatedUser() {
    var user = loadUser();
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    UserDto dto = userDto();
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    userService.update(ID, dto);

    verify(userRepository, times(1)).findById(ID);
    verify(userRepository, times(1)).save(any());
    verify(userRepository, times(0)).existsByPhoneNumberAndIdNot(dto.phoneNumber(), ID);
  }

  @Test
  void given_differentCpf_when_update_then_throwBadRequestException() {
    var user = loadUser();
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    UserDto dto = UserDto.builder()
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .cpf("62767960000")
        .build();

    Assertions.assertEquals("CPF não pode ser alterado",
        Assertions.assertThrows(BadRequestException.class,
            () -> userService.update(ID, dto)).getLocalizedMessage()
    );

    verify(userRepository, times(1)).findById(ID);
    verify(userRepository, times(0)).save(any());
  }

  @Test
  void given_differentEmailInDto_when_update_then_throwBadRequestException() {
    var user = loadUser();
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    UserDto dto = UserDto.builder()
        .email("outroemail@mail.com")
        .phoneNumber(user.getPhoneNumber())
        .cpf(user.getCpf())
        .build();

    Assertions.assertEquals("E-mail não pode ser alterado. Entre em contato com o suporte",
        Assertions.assertThrows(BadRequestException.class,
            () -> userService.update(ID, dto)).getLocalizedMessage()
    );

    verify(userRepository, times(1)).findById(ID);
    verify(userRepository, times(0)).save(any());
  }

  @Test
  void given_existingPhoneNumberFromAnotherUser_when_update_then_throwBadRequestException() {
    var user = loadUser();
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    UserDto dto = UserDto.builder()
        .email(user.getEmail())
        .phoneNumber("61999999998")
        .cpf(user.getCpf())
        .build();

    when(userRepository.existsByPhoneNumberAndIdNot(dto.phoneNumber(), ID)).thenReturn(true);

    Assertions.assertThrows(BadRequestException.class, () -> userService.update(ID, dto));

    verify(userRepository, times(1)).findById(ID);
    verify(userRepository, times(1)).existsByPhoneNumberAndIdNot(dto.phoneNumber(), ID);
    verify(userRepository, times(0)).save(any());
  }

  @Test
  void given_existingUser_when_delete_then_deleteUserSuccessfully() {
    User user = loadUser();
    when(userRepository.findById(ID)).thenReturn(Optional.of(user));

    userService.delete(ID);

    verify(userRepository, times(1)).findById(ID);
    verify(userRepository, times(1)).deleteById(ID);
  }

  @Test
  void given_nonExistentUserId_when_delete_then_throwNotFoundException() {
    when(userRepository.findById(ID)).thenReturn(Optional.empty());
    Assertions.assertThrows(NotFoundException.class, () -> userService.delete(ID));
  }
}