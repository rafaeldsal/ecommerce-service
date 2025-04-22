package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.MailIntegration;
import com.rafaeldsal.ws.minhaprata.mapper.UserCredentialsMapper;
import com.rafaeldsal.ws.minhaprata.mapper.UserMapper;
import com.rafaeldsal.ws.minhaprata.model.User;
import com.rafaeldsal.ws.minhaprata.model.UserCredentials;
import com.rafaeldsal.ws.minhaprata.repository.UserDetailsRepository;
import com.rafaeldsal.ws.minhaprata.repository.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.UserService;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserDetailsRepository userDetailsRepository;

  @Autowired
  private MailIntegration mailIntegration;

  @Value("${}")
  private String urlSiteMinhaPrata;

  @Override
  public Page<UserResponseDto> findAll(Integer page, Integer size, String sort, String name) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtils.getSortDirection(sort), "name"));

    Page<User> users;
    if (name != null && !name.trim().isEmpty()) {
      users = userRepository.findByNameContainingIgnoreCase(name, pageable);
    } else {
      users = userRepository.findAll(pageable);
    }
    return users.map(UserMapper::fromEntityToResponseDto);
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

    UserCredentials userCredentials = UserCredentialsMapper.fromDtoToEntity(dto);
    userDetailsRepository.save(userCredentials);

    String message = messageCreateAccount(user.getName());
    String subject = "\u2728 Seja bem-vindo ao Minha Prata!";

    mailIntegration.send(user.getEmail(), message, subject);

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

  private String messageCreateAccount(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("Olá, ").append(name).append("! \u2728 \n\n");
    sb.append("Seja muito bem-vindo(a) ao Minha Prata, a sua nova casa para encontrar joias de prata com elegância, carinho e qualidade.\n\n");
    sb.append("Seu cadastro foi realizado com sucesso, e agora você pode:\n\n");
    sb.append("\uD83D\uDC8D Explorar nossa coleção de brincos, anéis, braceletes e colares.\n");
    sb.append("\uD83D\uDED2 Adicionar seus produtos favoritos à sacola.\n");
    sb.append("\uD83D\uDCE6 Acompanhar seus pedidos diretamente pela sua conta.\n");
    sb.append("\uD83C\uDF81 Ficar por dentro de promoções e lançamentos exclusivos.\n\n");
    sb.append("Estamos muito felizes em ter você com a gente!!\n");
    sb.append("Se tiver qualquer dúvida, nossa equipe está pronta para ajudar.\n\n");
    sb.append("Com carinho,\n");
    sb.append("Equipe Minha Prata\n");
    sb.append(urlSiteMinhaPrata).append(" | @minhaprata");

    return sb.toString();
  }
}
