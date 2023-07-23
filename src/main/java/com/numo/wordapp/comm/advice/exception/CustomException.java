package com.numo.wordapp.comm.advice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomException extends RuntimeException{
    public CustomException(){
        super();
    }

    public CustomException(String msg){
        super(msg);
        log.info(msg);
    }

    public CustomException(ErrorCode errorCode){
        super(errorCode.getDescription());
        log.info("errorCode: {}, errorMsg: {}", errorCode.getCode(),errorCode.getDescription());
    }
}
