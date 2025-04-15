package com.rafaeldsal.ws.minhaprata.dto;

import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDto(
    Long id,

    @NotBlank(message = "não poder ser nulo ou vazio")
    @Size(min = 3, message = "valor mínimo igual a 3")
    String name,

    @Email(message = "inválido")
    String email,

    @CPF(message = "inválido")
    String cpf,

    @NotBlank(message = "não pode ser nulo ou vazio")
    @Size(min = 11, message = "valor mínimo igual a 11")
    String phoneNumber,

    @Past(message = "data deve estar no passado")
    LocalDate dtBirth,

    UserRole role,

    LocalDateTime dtCreated,

    LocalDateTime dtUpdated,

    @NotBlank(message = "atributo obrigatório")
    String password
) {

}
