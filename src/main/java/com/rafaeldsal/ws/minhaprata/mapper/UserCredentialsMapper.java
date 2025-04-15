package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.model.UserCredentials;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserCredentialsMapper {

  @Autowired
  private static PasswordEncoder passwordEncoder;

  public static UserCredentials fromDtoToEntity(UserDto dto) {
    if (dto == null) return null;

    String encryptedPassword = passwordEncoder.encode(dto.password());

    return UserCredentials.builder()
        .username(dto.email())
        .password(encryptedPassword)
        .role(dto.role() != null ? dto.role() : UserRole.USER)
        .build();
  }
}
