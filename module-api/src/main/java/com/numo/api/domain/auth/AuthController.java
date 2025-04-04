package com.numo.api.domain.auth;

import com.numo.api.domain.auth.dto.EmailRequestDto;
import com.numo.api.domain.auth.dto.LoginDto;
import com.numo.api.domain.user.dto.TokenDto;
import com.numo.api.domain.user.dto.VerificationRequestDto;
import com.numo.domain.auth.VerificationType;
import com.numo.api.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인, 토큰")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "코드 발송", description = "회원가입 및 비밀번호 변경을 위해 인증 코드 발송")
    @PostMapping(value = "/code/{type}")
    public ResponseEntity<String> sendEmailVerificationCode(@Valid @RequestBody EmailRequestDto userDto,
                                                            @PathVariable("type") VerificationType type) {
        return ResponseEntity.ok(authService.sendEmailVerificationCode(type, userDto));
    }

    @Operation(summary = "인증 코드 인증", description = "해당하는 이메일의 인증 코드를 인증(비밀번호 변경 또는 회원가입에 사용)")
    @PostMapping(value = "/code/verify")
    public ResponseEntity<String> verificationCode(@RequestBody VerificationRequestDto requestDto) {
        return ResponseEntity.ok(authService.verificationCode(requestDto));
    }

    @Operation(description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @Operation(description = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenDto){
        return ResponseEntity.ok(authService.reissue(tokenDto.refreshToken()));
    }

    @Operation(description = "로그아웃 / 리프레시 토큰정보를 삭제한다.")
    @DeleteMapping
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsImpl user) {
        authService.logout(user.getUserId());
        return ResponseEntity.noContent().build();
    }

}
