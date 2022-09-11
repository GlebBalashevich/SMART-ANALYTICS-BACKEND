package com.intexsoft.analytics.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException{

    public AuthenticationException(String message, HttpStatus httpStatus, String errorCode){
        super(message, httpStatus, errorCode);
    }

}
