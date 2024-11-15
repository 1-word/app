package com.numo.wordapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Builder;

@Builder
public record UpdateUserDto(
        @Null @Schema(description = "닉네임")
        String nickname,
        @Null @Schema(description = "썸네일 이미지 파일 아이디")
        String thumbnailId
) {
}
