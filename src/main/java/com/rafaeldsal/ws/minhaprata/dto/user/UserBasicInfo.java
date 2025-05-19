package com.rafaeldsal.ws.minhaprata.dto.user;

import lombok.Builder;

@Builder
public record UserBasicInfo(
    String email,
    String name
) {
}
