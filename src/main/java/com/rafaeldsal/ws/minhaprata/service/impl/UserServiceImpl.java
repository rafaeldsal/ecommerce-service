package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.UserMapper;
import com.rafaeldsal.ws.minhaprata.model.User;
import com.rafaeldsal.ws.minhaprata.repository.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public List<UserResponseDto> findAll() {
    return userRepository.findAll().stream()
        .map(UserMapper::fromEntityToResponseDto)
        .toList();
  }

  @Override
  public UserResponseDto findByID(Long id) {
    var user = getUser(id);
    return UserMapper.fromEntityToResponseDto(user);
  }

  @Override
  public UserResponseDto create(UserDto dto) {
    if (Objects.nonNull(dto.getId())) {
      throw new BadRequestException("userId não pode ser definido");
    }

    if (userRepository.existsByCpf(dto.getCpf()) ||
        userRepository.existsByEmail(dto.getEmail()) ||
        userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
      throw new BadRequestException("CPF, e-mail ou telefone já cadastrado");
    }

    User user = userRepository.save(UserMapper.fromDtoToEntity(dto));

    return UserMapper.fromEntityToResponseDto(user);
  }

  @Override
  public UserResponseDto update(Long id, UserDto dto) {
    User existingUser = getUser(id);

    if (!existingUser.getCpf().equals(dto.getCpf())) {
      throw new BadRequestException("CPF não pode ser alterado");
    }

    if (dto.getEmail() != null &&
        !dto.getEmail().equals(existingUser.getEmail()) &&
        userRepository.existsByEmail(dto.getEmail())) {
      throw new BadRequestException("E-mail já cadastrado");
    }

    if (dto.getPhoneNumber() != null &&
        !dto.getPhoneNumber().equals(existingUser.getPhoneNumber()) &&
        userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
      throw new BadRequestException("Telefone já cadastrado");
    }

    User user = UserMapper.fromDtoToEntity(dto);
    userRepository.save(user);
    return UserMapper.fromEntityToResponseDto(user);
  }

  @Override
  public void delete(Long id) {
    getUser(id);
    userRepository.deleteById(id);
  }

  private User getUser(Long id) {
    return userRepository.findById(id).orElseThrow(
        () -> new NotFoundException("Usuário não encontrado")
    );
  }
}
