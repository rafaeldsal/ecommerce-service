package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.user.UserDetailsDto;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.MailIntegration;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.model.redis.UserRecoveryCode;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserDetailsRepository;
import com.rafaeldsal.ws.minhaprata.repository.redis.UserRecoveryCodeRepository;
import com.rafaeldsal.ws.minhaprata.service.CustomUserService;
import com.rafaeldsal.ws.minhaprata.utils.PasswordUtils;
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

  private final UserRecoveryCodeRepository userRecoveryCodeRepository;

  private final MailIntegration mailIntegration;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDetailsRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
  }

  @Override
  public void sendRecoveryCode(UserRecoveryCode userRecoveryCodeRequest) {

    UserRecoveryCode userRecoveryCode;
    String code = String.format("%04d", new Random().nextInt(10000));

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

    mailIntegration.send(userRecoveryCodeRequest.getEmail(), messageRecoveryCode(userRecoveryCode.getName(), code), "Recuperação de conta - Minha Prata");

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

      mailIntegration.send(dto.email(), messagePasswordChangedSuccess(userCredentials.getName()), "Senha recuperada com sucesso!");
    }
  }


  private String messageRecoveryCode(String name, String code) {
    StringBuilder sb = new StringBuilder();
    sb.append("Olá, ").append(name).append("!\n");
    sb.append("Recebemos uma solicitação para redefinir a sua senha no Minha Prata.\n\n");
    sb.append("\uD83D\uDEE1\uFE0F Para continuar com o processo, utilize o código abaixo:\n\n");
    sb.append("\uD83D\uDD10 Código de recuperação: ").append("**").append(code).append("**\n\n");
    sb.append("Este código é válido por alguns minutos e deve ser inserido no campo solicitado para que você possa criar uma nova senha.\n");
    sb.append("Se você **não solicitou** essa recuperação, pode ignorar este e-mail com segurança — nenhuma alteração será feita na sua conta.\n\n");
    sb.append("Com carinho,\n");
    sb.append("Equipe Minha Prata\n");
    sb.append(urlSiteMinhaPrata).append(" | @minhaprata");

    return sb.toString();
  }

  private String messagePasswordChangedSuccess(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("Olá, ").append(name).append("!\n");
    sb.append("Sua senha foi alterada com sucesso. \uD83C\uDF89 \n\n");
    sb.append("Caso não tenha sido você quem fez essa alteração, recomendamos que entre em contato com a nossa equipe imediatamente para garantir a segurança da sua conta.\n");
    sb.append("Estamos sempre aqui para te ajudar!\n\n");
    sb.append("Com carinho,\n");
    sb.append("Equipe Minha Prata\n");
    sb.append(urlSiteMinhaPrata).append(" | @minhaprata");

    return sb.toString();
  }
}
