package com.numo.wordapp.controller;

import com.numo.wordapp.dto.user.ChangePasswordDto;
import com.numo.wordapp.dto.user.UpdateUserDto;
import com.numo.wordapp.dto.user.UserRequestDto;
import com.numo.wordapp.dto.user.UserDto;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<UserDto> signup(@RequestBody UserRequestDto userDto){
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @Operation(description = "비밀번호를 변경한다")
    @PutMapping(value = "/pw")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetailsImpl user,
                                               @RequestBody ChangePasswordDto passwordDto) {
        userService.updatePassword(user.getUser().getUserId(), passwordDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "회원정보를 가져온다")
    @GetMapping
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(userService.getUserInfo(user.getUser().getUserId()));
    }

    @Operation(description = "회원정보를 수정한다")
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@AuthenticationPrincipal UserDetailsImpl user,
                                              @RequestBody UpdateUserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(user.getUser().getUserId(), userDto));
    }

    @Operation(description = "회원을 탈퇴한다")
    @DeleteMapping
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserDetailsImpl user) {
        userService.withdraw(user.getUser().getUserId());
        return ResponseEntity.noContent().build();
    }

}
