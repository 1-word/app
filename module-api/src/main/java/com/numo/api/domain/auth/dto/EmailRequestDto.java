package com.numo.api.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailRequestDto(
        @Email
        @NotEmpty
        @Schema(description = "유저 아이디")
        String email
) {

}
