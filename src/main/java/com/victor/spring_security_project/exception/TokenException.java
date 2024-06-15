package com.victor.spring_security_project.exception;

public class TokenException extends RuntimeException{
    public TokenException(String msg) {
        super(msg);
    }
}
