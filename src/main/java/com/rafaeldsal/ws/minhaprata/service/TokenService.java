package com.rafaeldsal.ws.minhaprata.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

  String getToken(Authentication auth);

  Boolean isValid(String token);

  Long getUserId(String token);
}
