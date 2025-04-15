package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.LoginDto;
import com.rafaeldsal.ws.minhaprata.dto.TokenDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.service.AuthenticationService;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

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
