package com.numo.wordapp.security.controller;

import com.numo.wordapp.security.dto.UserDto;
import com.numo.wordapp.security.model.User;
import com.numo.wordapp.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto.Response signup(@RequestBody UserDto.Request userDto){
        return new UserDto.Response(userService.signup(userDto));
    }

    // HEADER로 토큰 정보 받아서 확인
    @GetMapping("/user")
    //USER, ADMIN 두가지 권한 모두 허용
    //@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public UserDto.Response getMyUserInfo(){
        //return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
        //return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
        return new UserDto.Response(userService.getMyUserWithAuthorities().get());
    }

    // // HEADER로 토큰 정보 받아서 확인
    @GetMapping("/user/{username}")
    // ADMIN권한만 허용
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public UserDto.Response getUserInfo(@PathVariable String username){
        return new UserDto.Response(userService.getUserWithAuthorities(username).get());
    }
}
