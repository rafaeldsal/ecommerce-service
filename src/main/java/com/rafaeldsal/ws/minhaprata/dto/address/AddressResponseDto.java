package com.rafaeldsal.ws.minhaprata.dto.address;

import lombok.Builder;

@Builder
public record AddressResponseDto(
    String id,
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String postalCode,
    String userId
) {
}