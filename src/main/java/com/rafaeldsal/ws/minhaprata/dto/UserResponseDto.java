package com.rafaeldsal.ws.minhaprata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

  private Long id;

  private String name;

  private String email;

  private String cpf;

  private String phoneNumber;

  private LocalDate dtBirth;

  private LocalDate dtCreated;

  private LocalDate dtUpdated;

}
