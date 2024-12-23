package com.numo.api.dto.user;

public record VerificationRequestDto(
        String email,
        String code
) {
}
