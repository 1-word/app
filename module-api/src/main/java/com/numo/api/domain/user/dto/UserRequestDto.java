package com.numo.api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(
        @Email
        @Schema(description = "유저 아이디")
        String email,
        @NotNull
        @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[\\d\\W]).{8,20}", message = "8~20자, 영문+숫자/특수문자를 포함해야 합니다.")
        @Schema(description = "비밀번호")
        String password,
        @NotNull
        @Schema(description = "닉네임")
        String nickname,
        @Schema(description = "썸네일 이미지 파일 경로")
        String profileImagePath
) {

}
