package com.numo.api.global.comm.exception;

import com.numo.api.global.comm.response.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<CommonResult> defaultException(HttpServletRequest request, Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResult.getFailResult());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<CommonResult> defaultRuntimeException(HttpServletRequest request, Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResult.getFailResult());
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    protected ResponseEntity<CommonResult> userNotFoundException(HttpServletRequest request, CustomException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                CommonResult.builder()
                        .code(e.errorCode.getCode())
                        .success(false)
                        .msg(e.errorCode.getDescription())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResult> methodArgumentNotValidException(HttpServletRequest request, BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                CommonResult.builder()
                        .code(400)
                        .success(false)
                        .msg(errorMessage)
                        .build()
        );
    }
}
