package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.UserMapper;
import com.rafaeldsal.ws.minhaprata.model.User;
import com.rafaeldsal.ws.minhaprata.repository.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.UserService;
import jakarta.transaction.Transactional;
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
    if (Objects.nonNull(dto.id())) {
      throw new BadRequestException("userId não pode ser definido");
    }

    if (userRepository.existsByCpf(dto.cpf()) ||
        userRepository.existsByEmail(dto.email()) ||
        userRepository.existsByPhoneNumber(dto.phoneNumber())) {
      throw new BadRequestException("CPF, e-mail ou telefone já cadastrado");
    }

    User user = userRepository.save(UserMapper.fromDtoToEntity(dto));

    return UserMapper.fromEntityToResponseDto(user);
  }

  @Override
  @Transactional
  public UserResponseDto update(Long id, UserDto dto) {
    User existingUser = getUser(id);

    if (!existingUser.getCpf().equals(dto.cpf())) {
      throw new BadRequestException("CPF não pode ser alterado");
    }

    if (dto.email() != null &&
        !dto.email().equals(existingUser.getEmail()) &&
        userRepository.existsByEmailAndIdNot(dto.email(), id)) {
      throw new BadRequestException("E-mail já cadastrado");
    }

    if (dto.phoneNumber() != null &&
        !dto.phoneNumber().equals(existingUser.getPhoneNumber()) &&
        userRepository.existsByPhoneNumberAndIdNot(dto.phoneNumber(), id)) {
      throw new BadRequestException("Telefone já cadastrado");
    }

    System.out.println("DTO data: " + dto.dtUpdated());
    System.out.println("User data: " + existingUser.getDtUpdated());

    System.out.println("DTO email: " + dto.email());
    System.out.println("User email: " + existingUser.getEmail());

    UserMapper.updateEntityFromDto(dto, existingUser);
    userRepository.save(existingUser);

    return UserMapper.fromEntityToResponseDto(existingUser);
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
