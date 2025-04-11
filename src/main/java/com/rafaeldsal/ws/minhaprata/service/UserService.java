package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {

  Page<UserResponseDto> findAll(Integer page, Integer size, String sort, String name);

  UserResponseDto findByID(Long id);

  UserResponseDto create(UserDto dto);

  UserResponseDto update(Long id, UserDto dto);

  void delete(Long id);
}
