package com.numo.wordapp.controller;

import com.numo.wordapp.dto.user.UserDto;
import com.numo.wordapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 컨트롤러")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(description = "회원가입")
    @PostMapping(value = "/signup")
    public UserDto.Response signup(@RequestBody UserDto.Request userDto){
        return new UserDto.Response(userService.signup(userDto));
    }

    //TODO 회원정보

    //TODO 회원탈퇴

    //TODO 로그아웃
}
