package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.user.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {

  Page<UserResponseDto> findAll(Integer page, Integer size, String sort, String name);

  UserResponseDto findByID(String userId);

  UserResponseDto create(UserDto dto);

  UserResponseDto update(String userId, UserDto dto);

  void delete(String userId);
}
