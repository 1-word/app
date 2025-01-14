package com.numo.api.domain.user.dto;

public record VerificationRequestDto(
        String email,
        String code
) {
}
