package com.numo.wordapp.comm.advice.exception;

import com.numo.wordapp.model.response.CommonResult;
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e){
        return responseService.getFailResult();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultRuntimeException(HttpServletRequest request, Exception e){
        return responseService.getFailResult();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    protected CommonResult userNotFoundException(HttpServletRequest request, CustomException e){
        return responseService.getFailResult(e.errorCode);
    }

    //로그인 실패 시 예외 처리
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.OK)
    protected CommonResult loginException(HttpServletRequest request, Exception e){
        return responseService.getFailResult(ErrorCode.BadCredentials);
    }
}
