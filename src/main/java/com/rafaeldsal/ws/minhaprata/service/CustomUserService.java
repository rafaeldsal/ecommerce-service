package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.UserDetailsDto;
import com.rafaeldsal.ws.minhaprata.model.redis.UserRecoveryCode;

public interface CustomUserService {

  void sendRecoveryCode(UserRecoveryCode userRecoveryCode);

  Boolean recoveryCodeIsValid(String recoveryCode, String email);

  void updatedPasswordByRecoveryCode(UserDetailsDto dto);
}
