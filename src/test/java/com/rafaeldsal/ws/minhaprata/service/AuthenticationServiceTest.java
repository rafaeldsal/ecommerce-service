package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.auth.LoginDto;
import com.rafaeldsal.ws.minhaprata.dto.auth.TokenDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {


  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private TokenService tokenService;

  @InjectMocks
  private AuthenticationServiceImpl authenticationService;

  @Test
  void given_auth_when_credentialsIsValid_then_returnToken() {
    LoginDto dto = LoginDto.builder()
        .username("rafael@email.com")
        .password("senha123")
        .build();
    Authentication authentication = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

    String expectedToken = "mocked-token";

    when(authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
    )).thenReturn(authentication);

    when(tokenService.getToken(authentication)).thenReturn(expectedToken);

    TokenDto result = authenticationService.auth(dto);

    assertNotNull(result);
    assertEquals(expectedToken, result.token());
    assertEquals("Bearer", result.type());

    verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
  }

  @Test
  void given_auth_when_authenticationFails_then_throwBadRequestException() {
    LoginDto dto = LoginDto.builder()
        .username("rafael@email.com")
        .password("wrongpassword")
        .build();
    when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password())))
        .thenThrow(new AuthenticationException("Invalid credentials") {});

    assertThrows(BadRequestException.class, () -> authenticationService.auth(dto));

    verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
  }

}