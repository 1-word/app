package com.numo.wordapp.security.controller;

import com.numo.wordapp.model.response.SingleResult;
import com.numo.wordapp.security.dto.LoginDto;
import com.numo.wordapp.security.dto.TokenDto;
import com.numo.wordapp.security.jwt.TokenProvider;
import com.numo.wordapp.security.service.UserService;
import com.numo.wordapp.service.ResponseService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ResponseService responseService;
    private final UserService userService;

    public AuthController(TokenProvider tokenProvider,
                          AuthenticationManagerBuilder authenticationManagerBuilder,
                          ResponseService responseService,
                          UserService userService){
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.responseService = responseService;
        this.userService = userService;
    }

/*    @PostMapping("authenticate")
    public SingleResult<TokenDto.response> authorize(@Valid @RequestBody LoginDto loginDto){
        System.out.println("authenticate start");
        //LoginDto의 username, password로 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //토큰을 이용하여 authenticate메소드가 실행될 때 loadUserByUsername 메소드 실행(CustomUserDetailsService.loadUserByUsername())
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //생성된 authentication 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //createToken메소드를 통해 JWT 토큰 생성
        //String jwt = tokenProvider.createToken(authentication);


        String jwt = tokenProvider.createTokenDto(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZTION_HEADER, "Bearer " + jwt);


        System.out.println(new TokenDto.response((jwt)));

        return responseService.getSingleResult();
    }*/

    /**
     * {@link com.numo.wordapp.security.service.impl.UserServiceImpl}
     * @param tokenDto
     * */
    @PostMapping("reissue")
    public SingleResult<TokenDto.response> reissue(@RequestBody TokenDto.request tokenDto){
        return responseService.getSingleResult(userService.reissue(tokenDto));
    }

    @PostMapping("login")
    public SingleResult<TokenDto.response> login(@RequestBody LoginDto loginDto){
        SecurityContextSave(loginDto);
        return responseService.getSingleResult(userService.login(loginDto));
    }

    //로그인 시 SecurityContext 객체에 저장..
    public void SecurityContextSave(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUser_id(), loginDto.getPassword());

        //토큰을 이용하여 authenticate메소드가 실행될 때 loadUserByUsername 메소드 실행(CustomUserDetailsService.loadUserByUsername())
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //생성된 authentication 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
