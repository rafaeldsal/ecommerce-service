package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.user.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.user.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.MailIntegration;
import com.rafaeldsal.ws.minhaprata.mapper.user.UserCredentialsMapper;
import com.rafaeldsal.ws.minhaprata.mapper.user.UserMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserDetailsRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.UserService;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final UserDetailsRepository userDetailsRepository;

  private final MailIntegration mailIntegration;

  @Value("${webservices.minhaprata.default.url.site}")
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
  public UserResponseDto findByID(String userId) {
    var user = getUser(userId);
    return UserMapper.fromEntityToResponseDto(user);
  }

  @Override
  @Transactional
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
  public UserResponseDto update(String userId, UserDto dto) {
    User existingUser = getUser(userId);

    if (!existingUser.getCpf().equals(dto.cpf())) {
      throw new BadRequestException("CPF não pode ser alterado");
    }

    if (!existingUser.getEmail().equals(dto.email())) {
      throw new BadRequestException("E-mail não pode ser alterado. Entre em contato com o suporte");
    }

    if (dto.phoneNumber() != null &&
        !dto.phoneNumber().equals(existingUser.getPhoneNumber()) &&
        userRepository.existsByPhoneNumberAndIdNot(dto.phoneNumber(), userId)) {
      throw new BadRequestException("Telefone já cadastrado");
    }

    UserMapper.updateEntityFromDto(dto, existingUser);
    userRepository.save(existingUser);

    return UserMapper.fromEntityToResponseDto(existingUser);
  }

  @Override
  public void delete(String userId) {
    getUser(userId);
    userRepository.deleteById(userId);
  }

  private User getUser(String id) {
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
