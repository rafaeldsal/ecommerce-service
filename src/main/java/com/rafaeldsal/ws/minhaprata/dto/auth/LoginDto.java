package com.rafaeldsal.ws.minhaprata.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
    @NotBlank(message = "atributo obrigatório")
    String username,

    @NotBlank(message = "atributo obrigatório")
    String password
) {
}
