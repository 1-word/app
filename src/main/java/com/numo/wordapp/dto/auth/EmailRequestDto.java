package com.numo.wordapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record EmailRequestDto(
        @Email
        @Schema(description = "유저 아이디")
        String email
) {

}
