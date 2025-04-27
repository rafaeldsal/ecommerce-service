package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.auth.LoginDto;
import com.rafaeldsal.ws.minhaprata.dto.auth.TokenDto;

public interface AuthenticationService {

  TokenDto auth(LoginDto dto);
}
