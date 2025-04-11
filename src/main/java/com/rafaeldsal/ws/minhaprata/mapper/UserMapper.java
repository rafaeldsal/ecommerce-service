package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.model.User;
import com.rafaeldsal.ws.minhaprata.model.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserMapper {

  public static User fromDtoToEntity(UserDto dto) {

    if (dto == null) return null;

    return User.builder()
        .id(dto.id())
        .name(dto.name())
        .email(dto.email())
        .cpf(dto.cpf())
        .phoneNumber(dto.phoneNumber())
        .dtBirth(dto.dtBirth())
        .dtCreated(dto.dtCreated() != null ? dto.dtCreated() : LocalDateTime.now())
        .dtUpdated(dto.dtUpdated() != null ? dto.dtUpdated() : LocalDateTime.now())
        .role(dto.role() != null ? dto.role() : UserRole.USER)
        .build();
  }

  public static void updateEntityFromDto(UserDto dto, User user) {
    if (dto == null || user == null) return;

    user.setName(dto.name() != null ? dto.name() : user.getName());
    user.setEmail(dto.email() != null ? dto.email() : user.getEmail());
    user.setPhoneNumber(dto.phoneNumber() != null ? dto.phoneNumber() : user.getPhoneNumber());
    user.setDtBirth(dto.dtBirth() != null ? dto.dtBirth() : user.getDtBirth());
    user.setDtUpdated(LocalDateTime.now());
  }

  public static UserResponseDto fromEntityToResponseDto(User user) {
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

}
