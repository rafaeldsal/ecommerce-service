package com.rafaeldsal.ws.minhaprata.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequestDto(
    String id,

    @NotBlank(message = "não pode ser nulo ou vazio")
    @Pattern(regexp = "\\d{8}", message = "CEP inválido. Deve conter exatamente 8 dígitos numéricos")
    String postalCode,

    String complement,

    String number,

    @NotBlank(message = "não pode ser nulo ou vazio")
    String userId
) {
}
