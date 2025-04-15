package com.rafaeldsal.ws.minhaprata.dto;

import lombok.Builder;

@Builder
public record TokenDto(
    String token,
    String type
) {
}
