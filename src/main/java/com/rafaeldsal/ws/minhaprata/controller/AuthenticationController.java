package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.LoginDto;
import com.rafaeldsal.ws.minhaprata.dto.TokenDto;
import com.rafaeldsal.ws.minhaprata.dto.UserDetailsDto;
import com.rafaeldsal.ws.minhaprata.model.redis.UserRecoveryCode;
import com.rafaeldsal.ws.minhaprata.service.AuthenticationService;
import com.rafaeldsal.ws.minhaprata.service.CustomUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private CustomUserService customUserService;

  @PostMapping
  public ResponseEntity<TokenDto> auth(@RequestBody @Valid LoginDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(authenticationService.auth(dto));
  }

  @PostMapping("/recovery-code/send")
  public ResponseEntity<?> sendRecoveryCode(@RequestBody @Valid UserRecoveryCode userRecoveryCode) {
    customUserService.sendRecoveryCode(userRecoveryCode);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/recovery-code")
  public ResponseEntity<?> recoveryCodeIsValid(@RequestParam("recoveryCode") String recoveryCode,
                                               @RequestParam("email") String email) {
    return ResponseEntity.status(HttpStatus.OK).body(customUserService.recoveryCodeIsValid(recoveryCode, email));
  }

  @PatchMapping("/recovery-code/password")
  public ResponseEntity<?> updatedPasswordByRecoveryCode(@RequestBody @Valid UserDetailsDto dto) {
    customUserService.updatedPasswordByRecoveryCode(dto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
