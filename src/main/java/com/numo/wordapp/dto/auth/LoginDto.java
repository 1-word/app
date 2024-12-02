package com.numo.wordapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LoginDto(@NotNull
                       @Schema(description = "유저 아이디")
                       String email,
                       @NotNull
                       @Schema(description = "유저 패스워드")
                       String password) {
}