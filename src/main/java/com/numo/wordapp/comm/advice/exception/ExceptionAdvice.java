package com.numo.wordapp.comm.advice.exception;

import com.numo.wordapp.model.CommonResult;
import com.numo.wordapp.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   //보내줄 status값 설정
    protected CommonResult defaultException(HttpServletRequest request, Exception e){
        return responseService.getFailResult();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   //보내줄 status값 설정
    protected CommonResult defaultRuntimeException(HttpServletRequest request, Exception e){
        return responseService.getFailResult();
    }

    @ExceptionHandler(UserNotFoundCException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundCException e){
        return responseService.getFailResult();
    }

    //로그인이 안된 상태에서 접근하려할 때 예외처리
    @ExceptionHandler(TokenCException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)   //보내줄 status값 설정
    protected CommonResult TokenException(HttpServletRequest request, Exception e){
        return responseService.getFailResult(ErrorCode.OperationNotAuthorized);
    }

    //로그인 실패 시 예외 처리
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.OK)   //보내줄 status값 설정
    protected CommonResult loginException(HttpServletRequest request, Exception e){
        return responseService.getFailResult(ErrorCode.LoginFail);
    }
}
