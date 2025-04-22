package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.utils.PasswordUtils;

public class UserCredentialsMapper {

  public static UserCredentials fromDtoToEntity(UserDto dto) {
    if (dto == null) return null;

    String encryptedPassword = PasswordUtils.encode(dto.password());

    return UserCredentials.builder()
        .username(dto.email())
        .password(encryptedPassword)
        .name(dto.name())
        .role(dto.role() != null ? dto.role() : UserRole.USER)
        .build();
  }
}
