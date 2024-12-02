package com.numo.wordapp.dto.user;

public record VerificationRequestDto(
        String email,
        String code
) {
}
