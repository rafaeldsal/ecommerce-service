package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.user.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.user.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<Page<UserResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                       @RequestParam(value = "sort", defaultValue = "ASC") String sort,
                                                       @RequestParam(value = "name", required = false) String name) {
    Page<UserResponseDto> users = userService.findAll(page, size, sort, name);
    return ResponseEntity.ok(users);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("{userId}")
  public ResponseEntity<UserResponseDto> findById(@PathVariable("userId") String userId) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findByID(userId));
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
  }

  @PutMapping("{userId}")
  public ResponseEntity<UserResponseDto> update(@PathVariable("userId") String userId, @Valid @RequestBody UserDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, dto));
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") String userId) {
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
