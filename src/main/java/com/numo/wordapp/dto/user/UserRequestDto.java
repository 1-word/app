package com.numo.wordapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotNull
        @Size(min = 3, max = 50)
        @Schema(description = "유저 아이디")
        String email,
        @NotNull
        @Size(min = 3, max = 100)
        @Schema(description = "비밀번호")String password,
        @NotNull
        @Size(min = 3, max = 50)
        @Schema(description = "닉네임")
        String nickname
) {

}
