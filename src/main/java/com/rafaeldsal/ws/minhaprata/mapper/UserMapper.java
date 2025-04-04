package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.model.User;

import java.time.LocalDate;

public class UserMapper {

  public static User fromDtoToEntity(UserDto dto) {
    return User.builder()
        .id(dto.getId())
        .name(dto.getName())
        .email(dto.getEmail())
        .cpf(dto.getCpf())
        .phoneNumber(dto.getPhoneNumber())
        .dtBirth(dto.getDtBirth())
        .dtCreated(dto.getDtCreated() != null ? dto.getDtCreated() : LocalDate.now())
        .dtUpdated(dto.getDtUpdated() != null ? dto.getDtUpdated() : LocalDate.now())
        .role(dto.getRole())
        .build();
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
