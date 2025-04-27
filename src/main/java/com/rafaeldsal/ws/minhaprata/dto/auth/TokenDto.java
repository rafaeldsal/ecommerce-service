package com.rafaeldsal.ws.minhaprata.dto.auth;

import lombok.Builder;

@Builder
public record TokenDto(
    String token,
    String type
) {
}
