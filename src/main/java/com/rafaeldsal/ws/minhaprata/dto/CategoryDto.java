package com.rafaeldsal.ws.minhaprata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CategoryDto(
    Long id,

    @NotBlank(message = "não pode ser nulo ou vazio")
    @Size(min = 4, max = 15, message = "deve ter tamanho entre 4 e 15")
    String name,

    @Size(min = 5, max = 255, message = "deve ter tamanho entre 5 e 255")
    String description
) {

}