package com.rafaeldsal.ws.minhaprata.configuration.security;

import com.rafaeldsal.ws.minhaprata.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AuthenticationAspect {

  @Before("@annotation(com.rafaeldsal.ws.minhaprata.configuration.security.RequireAuthentication)")
  public void checkAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      log.warn("Tentativa de acesso sem autenticação");
      throw new UnauthorizedException("Usuário não autenticado");
    }
  }
}
