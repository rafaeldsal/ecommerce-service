package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.user.UserDetailsDto;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.MailIntegration;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.model.redis.UserRecoveryCode;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserDetailsRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.repository.redis.UserRecoveryCodeRepository;
import com.rafaeldsal.ws.minhaprata.service.CodeGenerator;
import com.rafaeldsal.ws.minhaprata.service.CustomUserService;
import com.rafaeldsal.ws.minhaprata.utils.EmailMessageBuilder;
import com.rafaeldsal.ws.minhaprata.utils.PasswordUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, CustomUserService {

  @Value("${webservices.minhaprata.default.url.site}")
  private String urlSiteMinhaPrata;

  @Value("${webservices.minhaprata.recoveryCode.timeout}")
  private String recoveryCodeTimeout;

  private final UserDetailsRepository userDetailsRepository;
  private final UserRepository userRepository;
  private final UserRecoveryCodeRepository userRecoveryCodeRepository;
  private final MailIntegration mailIntegration;
  private final CodeGenerator codeGenerator;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDetailsRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
  }

  @Override
  @Transactional
  public void sendRecoveryCode(UserRecoveryCode userRecoveryCodeRequest) {

    UserRecoveryCode userRecoveryCode;
    String code = codeGenerator.generate();

    var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(userRecoveryCodeRequest.getEmail());

    if (userRecoveryCodeOpt.isEmpty()) {
      userDetailsRepository.findByUsername(userRecoveryCodeRequest.getEmail())
          .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

      userRecoveryCode = new UserRecoveryCode();
      userRecoveryCode.setEmail(userRecoveryCodeRequest.getEmail());
      userRecoveryCode.setName(userRecoveryCodeRequest.getName());
    } else {
      userRecoveryCode = userRecoveryCodeOpt.get();
    }
    userRecoveryCode.setCode(code);
    userRecoveryCode.setCreationDate(LocalDateTime.now());

    userRecoveryCodeRepository.save(userRecoveryCode);

    var user = userRepository.findByEmail(userRecoveryCodeRequest.getEmail())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    mailIntegration.send(userRecoveryCodeRequest.getEmail(), EmailMessageBuilder.buildRecoveryMessage(user.name(), code, urlSiteMinhaPrata), "Recuperação de conta - Minha Prata");
  }

  @Override
  public Boolean recoveryCodeIsValid(String recoveryCode, String email) {
    var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    LocalDateTime timeout = userRecoveryCodeOpt.getCreationDate().plusMinutes(Long.parseLong(recoveryCodeTimeout));
    LocalDateTime timeoutNow = LocalDateTime.now();

    return recoveryCode.equals(userRecoveryCodeOpt.getCode()) && timeoutNow.isBefore(timeout);
  }

  @Override
  public void updatedPasswordByRecoveryCode(UserDetailsDto dto) {
    if(recoveryCodeIsValid(dto.recoveryCode(), dto.email())) {
      var userDetails = userDetailsRepository.findByUsername(dto.email());

      UserCredentials userCredentials = userDetails.get();

      userCredentials.setPassword(PasswordUtils.encode(dto.password()));
      userDetailsRepository.save(userCredentials);

      var user = userRepository.findByEmail(dto.email()).get();

      mailIntegration.send(dto.email(), EmailMessageBuilder.buildMessagePasswordChangedSuccess(user.name(), urlSiteMinhaPrata), "Senha recuperada com sucesso!");
    }
  }
}
