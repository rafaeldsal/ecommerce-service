package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> findAll() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
  }

  @GetMapping("{id}")
  public ResponseEntity<UserResponseDto> findById(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findByID(id));
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
  }

  @PutMapping("{id}")
  public ResponseEntity<UserResponseDto> update(@PathVariable("id") Long id, @RequestBody UserDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, dto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    userService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
