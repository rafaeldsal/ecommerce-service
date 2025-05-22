package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.service.impl.TokenServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @Mock
  private Authentication authentication;
  @Mock
  private UserCredentials userCredentials;

  @InjectMocks
  private TokenServiceImpl tokenService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(tokenService, "secret", Base64.getEncoder().encodeToString("my-secret-key-1234567890-1234567890".getBytes()));
    ReflectionTestUtils.setField(tokenService, "expiration", "3600000");
  }

  @Test
  void given_getToken_when_validAuthentication_then_returnValidToken() {
    Authentication auth = mock(Authentication.class);
    UserCredentials credentials = new UserCredentials();
    credentials.setId(IdGenerator.UUIDGenerator("credentials"));
    when(auth.getPrincipal()).thenReturn(credentials);

    String token = tokenService.getToken(auth);

    assertNotNull(token);
    assertEquals(3, token.split("\\.").length);
  }

  @Test
  void given_isValid_when_validateGeneratedToken_then_returnTrue() {
    Authentication auth = mock(Authentication.class);
    UserCredentials credentials = new UserCredentials();
    credentials.setId(IdGenerator.UUIDGenerator("credentials"));
    when(auth.getPrincipal()).thenReturn(credentials);

    String token = tokenService.getToken(auth);
    boolean isValid = tokenService.isValid(token);

    assertTrue(isValid);
  }

  @Test
  void given_isValid_when_validateGeneratedToken_then_returnFalse() {
    boolean isValid = tokenService.isValid("invalid.token");

    assertFalse(isValid);
  }

  @Test
  void given_getUserId_when_invalidToken_then_throwJwtException() {
    assertThrows(JwtException.class, () -> tokenService.getUserId("invalid.token"));
  }

  @Test
  void given_getUserId_when_existsUserId_then_returnUserId() {
    Authentication auth = mock(Authentication.class);
    UserCredentials credentials = new UserCredentials();
    String userCredentialsId = IdGenerator.UUIDGenerator("credentials");
    credentials.setId(userCredentialsId);
    when(auth.getPrincipal()).thenReturn(credentials);

    String token = tokenService.getToken(auth);
    String userId = tokenService.getUserId(token);

    assertEquals(userCredentialsId, userId);
  }

}