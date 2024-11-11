package com.numo.wordapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordDto(
        @NotNull
        @Schema(description = "현재 비밀번호")
        String oldPassword,
        @NotNull
        @Schema(description = "변경할 비밀번호")
        String newPassword
) {
}
