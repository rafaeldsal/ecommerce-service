package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.user.UserBasicInfo;
import com.rafaeldsal.ws.minhaprata.dto.user.UserDetailsDto;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.integration.MailIntegration;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.model.redis.UserRecoveryCode;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserDetailsRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.repository.redis.UserRecoveryCodeRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.UserDetailsServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.EmailMessageBuilder;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import com.rafaeldsal.ws.minhaprata.utils.PasswordUtils;
import org.apache.zookeeper.Op;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

  private static final String USERNAME = "rafael@email.com";
  private static final String RECOVERY_CODE = "1234";
  private static final String PASSWORD = PasswordUtils.encode("senha123");
  private static final String ID_USER_CREDENTIALS = IdGenerator.UUIDGenerator("credentials");
  private static final String SITE_PROJECT = "www.minhaprata.com.br";

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserDetailsRepository userDetailsRepository;

  @Mock
  private UserRecoveryCodeRepository userRecoveryCodeRepository;

  @Mock
  private CodeGenerator codeGenerator;

  @Mock
  private MailIntegration mailIntegration;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Test
  void given_loadUserByUsername_when_thereIsUsername_then_returnUserCredentials() {
    UserRole role = UserRole.USER;
    UserCredentials userCredentials = getUserCredentials(role);

    when(userDetailsRepository.findByUsername(USERNAME)).thenReturn(Optional.of(userCredentials));

    UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

    assertNotNull(userDetails);
    assertEquals(USERNAME, userDetails.getUsername());
    assertEquals(PASSWORD, userDetails.getPassword());

    verify(userDetailsRepository, times(1)).findByUsername(USERNAME);
  }

  @Test
  void given_loadUserByUsername_when_usernameNotExists_then_throwUsernameNotFoundException() {
    String username = "nonexistent@email.com";

    when(userDetailsRepository.findByUsername(username)).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    verify(userDetailsRepository, times(1)).findByUsername(username);
  }

  @Test
  void given_sendRecoveryCode_when_userExistsAndNoCodeExists_then_createAndSendNewCode() {
    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();
    UserBasicInfo user = getUserBasicInfo();
    ReflectionTestUtils.setField(userDetailsService, "urlSiteMinhaPrata", SITE_PROJECT);

    when(codeGenerator.generate()).thenReturn(RECOVERY_CODE);
    when(userRecoveryCodeRepository.findByEmail(USERNAME)).thenReturn(Optional.of(userRecoveryCode));
    when(userRepository.findByEmail(user.email())).thenReturn(Optional.of(user));
    when(userRecoveryCodeRepository.save(any(UserRecoveryCode.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    userDetailsService.sendRecoveryCode(userRecoveryCode);

    verify(userRecoveryCodeRepository, times(1)).save(any());
    verify(userRepository, times(1)).findByEmail(USERNAME);
    verify(mailIntegration, times(1)).send(
        eq(USERNAME), eq(EmailMessageBuilder.buildRecoveryMessage(user.name(), RECOVERY_CODE, SITE_PROJECT)), eq("Recuperação de conta - Minha Prata"));
  }

  @Test
  void given_sendRecoveryCode_when_userCredentialsNotExists_then_throwNotFoundException() {
    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();
    UserRole role = UserRole.USER;
    UserCredentials userCredentials = getUserCredentials(role);

    when(userDetailsRepository.findByUsername(userCredentials.getUsername())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userDetailsService.sendRecoveryCode(userRecoveryCode));

    verify(userDetailsRepository, times(1)).findByUsername(any());
    verify(userRecoveryCodeRepository, times(0)).save(any());
    verify(userRepository, times(0)).findByEmail(USERNAME);
    verify(mailIntegration, times(0)).send(any(), anyString(), any());
  }

  @Test
  void given_sendRecoveryCode_when_userNotFound_then_throwNotFoundException() {
    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();
    UserRole role = UserRole.USER;
    UserCredentials userCredentials = getUserCredentials(role);

    when(userDetailsRepository.findByUsername(userCredentials.getUsername())).thenReturn(Optional.of(userCredentials));
    assertThrows(NotFoundException.class, () -> userDetailsService.sendRecoveryCode(userRecoveryCode));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(userRecoveryCode.getEmail());
    verify(userDetailsRepository, times(1)).findByUsername(userCredentials.getUsername());
    verify(userRepository, times(1)).findByEmail(userRecoveryCode.getEmail());
    verify(mailIntegration, times(0)).send(any(), anyString(), any());
  }

  @Test
  void given_recoveryCodeIsValid_when_codeMatchesAndNotExpired_then_returnTrue() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();
    when(userRecoveryCodeRepository.findByEmail(userRecoveryCode.getEmail())).thenReturn(Optional.of(userRecoveryCode));

    assertTrue(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE, USERNAME));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME);
  }

  @Test
  void given_recoveryCodeIsValid_when_codeIsInvalidButNotExpired_then_returnFalse() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();
    when(userRecoveryCodeRepository.findByEmail(userRecoveryCode.getEmail())).thenReturn(Optional.of(userRecoveryCode));

    assertFalse(userDetailsService.recoveryCodeIsValid("invalidCode", USERNAME));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME);
  }

  @Test
  void given_recoveryCodeIsValid_when_userNotFound_then_throwNotFoundException() {
    when(userRecoveryCodeRepository.findByEmail(USERNAME)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> userDetailsService.recoveryCodeIsValid(RECOVERY_CODE, USERNAME));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(any());
  }

  @Test
  void given_updatedPasswordByRecoveryCode_when_codeIsValid_then_updatePasswordAndSendConfirmation() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    ReflectionTestUtils.setField(userDetailsService, "urlSiteMinhaPrata", SITE_PROJECT);
    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();
    when(userRecoveryCodeRepository.findByEmail(USERNAME)).thenReturn(Optional.of(userRecoveryCode));


    UserRole role = UserRole.USER;
    UserCredentials userCredentials = getUserCredentials(role);
    when(userDetailsRepository.findByUsername(USERNAME)).thenReturn(Optional.of(userCredentials));
    when(userDetailsRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    UserBasicInfo user = getUserBasicInfo();
    when(userRepository.findByEmail(USERNAME)).thenReturn(Optional.of(user));

    UserDetailsDto dto = UserDetailsDto.builder()
        .email(USERNAME)
        .recoveryCode(RECOVERY_CODE)
        .password(PASSWORD)
        .build();
    userDetailsService.updatedPasswordByRecoveryCode(dto);

    verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME);
    verify(userRepository, times(1)).findByEmail(USERNAME);
    verify(userDetailsRepository, times(1)).findByUsername(USERNAME);
    verify(userDetailsRepository, times(1)).save(any());
    verify(mailIntegration, times(1)).send(
        eq(USERNAME), eq(EmailMessageBuilder.buildMessagePasswordChangedSuccess(user.name(), SITE_PROJECT)), eq("Senha recuperada com sucesso!"));
  }

  private UserRecoveryCode getUserRecoveryCode() {
    return UserRecoveryCode.builder()
        .id(UUID.randomUUID().toString())
        .name("Rafael")
        .email(USERNAME)
        .code(RECOVERY_CODE)
        .creationDate(LocalDateTime.now())
        .build();
  }

  private UserBasicInfo getUserBasicInfo() {
    return UserBasicInfo.builder()
        .email(USERNAME)
        .name("Rafael Souza")
        .build();
  }

  private UserCredentials getUserCredentials(UserRole role) {
    return UserCredentials.builder()
        .id(ID_USER_CREDENTIALS)
        .username(USERNAME)
        .password(PASSWORD)
        .role(role)
        .build();
  }
}