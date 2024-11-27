package com.numo.wordapp.controller;

import com.numo.wordapp.dto.user.LoginDto;
import com.numo.wordapp.dto.user.TokenDto;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.user.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
