package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.auth.LoginDto;
import com.rafaeldsal.ws.minhaprata.dto.auth.TokenDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.service.AuthenticationService;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;

  private final TokenService tokenService;

  @Override
  public TokenDto auth(LoginDto dto) {
    try {
      UsernamePasswordAuthenticationToken userPassAuth = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
      Authentication authentication = authenticationManager.authenticate(userPassAuth);
      String token = tokenService.getToken(authentication);
      return TokenDto.builder()
          .token(token)
          .type("Bearer")
          .build();
    } catch (AuthenticationException e) {
      throw new BadRequestException("Erro ao formatar o token - " + e.getMessage());
    }
  }
}
