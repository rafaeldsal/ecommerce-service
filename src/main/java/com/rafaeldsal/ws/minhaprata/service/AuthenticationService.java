package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.LoginDto;
import com.rafaeldsal.ws.minhaprata.dto.TokenDto;

public interface AuthenticationService {

  TokenDto auth(LoginDto dto);
}
