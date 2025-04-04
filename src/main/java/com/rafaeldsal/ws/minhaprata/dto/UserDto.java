package com.rafaeldsal.ws.minhaprata.dto;

import com.rafaeldsal.ws.minhaprata.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  private Long id;

  @NotBlank(message = "não poder ser nulo ou vazio")
  @Size(min = 3, message = "valor mínimo igual a 3")
  private String name;

  @Email(message = "inválido")
  private String email;

  @CPF(message = "inválido")
  private String cpf;

  @NotBlank(message = "não pode ser nulo ou vazio")
  @Size(min = 11, message = "valor mínimo igual a 11")
  private String phoneNumber;

  @Past(message = "data deve estar no passado")
  private LocalDate dtBirth;

  private UserRole role = UserRole.USER;

  private LocalDate dtCreated = LocalDate.now();

  private LocalDate dtUpdated = LocalDate.now();

}
