package com.rafaeldsal.ws.minhaprata.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDetailsDto(

    @Email(message = "campo inválido")
    String email,

    String recoveryCode,

    @NotBlank(message = "campo inválido")
    String password
) {
}
