package com.numo.api.comm.exception;

public class TokenCException extends RuntimeException {
    public TokenCException() {
        super();
    }

    public TokenCException(String msg) {
        System.out.println(msg);
        //super(msg);
    }
}