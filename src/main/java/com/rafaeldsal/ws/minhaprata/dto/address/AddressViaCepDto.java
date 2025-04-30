package com.rafaeldsal.ws.minhaprata.dto.address;

public record AddressViaCepDto(
    String cep,
    String logradouro,
    String estado,
    String bairro,
    String localidade
) {
}
