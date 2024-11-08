package com.numo.wordapp.security.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    private String user_id;

    @NotNull
    private String password;
}
