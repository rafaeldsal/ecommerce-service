package com.rafaeldsal.ws.minhaprata.mapper.user;

import com.rafaeldsal.ws.minhaprata.dto.user.UserDto;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import com.rafaeldsal.ws.minhaprata.utils.PasswordUtils;

public class UserCredentialsMapper {

  public static UserCredentials fromDtoToEntity(UserDto dto) {
    if (dto == null) return null;

    String encryptedPassword = PasswordUtils.encode(dto.password());

    return UserCredentials.builder()
        .id(IdGenerator.UUIDGenerator("credentials"))
        .role(dto.role() != null ? dto.role() : UserRole.USER)
        .username(dto.email())
        .password(encryptedPassword)
        .build();
  }
}
