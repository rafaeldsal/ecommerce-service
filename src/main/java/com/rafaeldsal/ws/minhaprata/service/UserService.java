package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;

import java.util.List;

public interface UserService {

  List<UserResponseDto> findAll();

  UserResponseDto findByID(Long id);

  UserResponseDto create(UserDto dto);

  UserResponseDto update(Long id, UserDto dto);

  void delete(Long id);
}
