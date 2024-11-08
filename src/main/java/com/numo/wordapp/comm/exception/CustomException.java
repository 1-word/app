package com.numo.wordapp.comm.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomException extends RuntimeException{
    ErrorCode errorCode;
    public CustomException() {
        super();
        Exception e = new Exception();
    }

    public CustomException(String msg){
        super(msg);
        log.info(msg);
    }

    public CustomException(ErrorCode errorCode){
        super(errorCode.getRemark());
        this.errorCode = errorCode;
        log.info("errorCode: {}, errorMsg {}", errorCode.getCode(), errorCode.getRemark());
    }
}
