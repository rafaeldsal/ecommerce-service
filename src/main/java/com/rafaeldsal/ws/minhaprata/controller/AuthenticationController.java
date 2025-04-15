package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.LoginDto;
import com.rafaeldsal.ws.minhaprata.dto.TokenDto;
import com.rafaeldsal.ws.minhaprata.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping
  public ResponseEntity<TokenDto> auth(@RequestBody @Valid LoginDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(authenticationService.auth(dto));
  }
}
