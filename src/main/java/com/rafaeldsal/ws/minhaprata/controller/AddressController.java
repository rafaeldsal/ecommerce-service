package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.address.AddressRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.address.AddressResponseDto;
import com.rafaeldsal.ws.minhaprata.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout/address")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @PostMapping
  public ResponseEntity<AddressResponseDto> create(@Valid @RequestBody AddressRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(dto));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<AddressResponseDto> findById(@PathVariable("userId") String userId) {
    return ResponseEntity.status(HttpStatus.OK).body(addressService.findById(userId));
  }

}
