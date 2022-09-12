package com.numo.wordapp.comm.advice.exception;

public class UserNotFoundCException extends RuntimeException{
    public UserNotFoundCException(){
        super();
    }

    public UserNotFoundCException(String msg){
        super(msg);
    }
}
