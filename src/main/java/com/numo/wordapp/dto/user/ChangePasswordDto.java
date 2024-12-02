package com.numo.wordapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordDto(
        @Email
        String email,
        @Schema(description = "현재 비밀번호")
        String oldPassword,
        @NotNull
        @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[\\d\\W]).{8,20}", message = "8~20자, 영문+숫자/특수문자를 포함해야 합니다.")
        @Schema(description = "변경할 비밀번호")
        String newPassword
) {
}
