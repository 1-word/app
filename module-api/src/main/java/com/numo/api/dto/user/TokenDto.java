package com.numo.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TokenDto(@Schema(description = "엑세스 토큰") String accessToken,
                       @Schema(description = "리프레시 토큰") String refreshToken) {
}