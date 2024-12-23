package com.numo.api.controller;

import com.numo.domain.user.dto.UpdateUserDto;
import com.numo.api.dto.user.ChangePasswordDto;
import com.numo.api.dto.user.UserDto;
import com.numo.api.dto.user.UserRequestDto;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(description = "회원가입")
    @PostMapping(value = "/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserRequestDto userDto){
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @Operation(description = "비밀번호를 변경한다(비로그인)")
    @PutMapping(value = "/pw/reset")
    public ResponseEntity<Void> changePasswordWithoutLogin(@Valid @RequestBody ChangePasswordDto passwordDto) {
        userService.updatePassword(passwordDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "비밀번호를 변경한다")
    @PutMapping(value = "/pw")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetailsImpl user,
                                               @Valid @RequestBody ChangePasswordDto passwordDto) {
        userService.updatePassword(user.getUserId(), passwordDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "회원정보를 가져온다")
    @GetMapping
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(userService.getUserInfo(user.getUserId()));
    }

    @Operation(description = "회원정보를 수정한다")
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@AuthenticationPrincipal UserDetailsImpl user,
                                              @RequestBody UpdateUserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(user.getUserId(), userDto));
    }

    @Operation(description = "회원을 탈퇴한다")
    @DeleteMapping
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserDetailsImpl user) {
        userService.withdraw(user.getUserId());
        return ResponseEntity.noContent().build();
    }

}
