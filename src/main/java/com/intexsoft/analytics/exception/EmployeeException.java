package com.intexsoft.analytics.exception;

import org.springframework.http.HttpStatus;

public class EmployeeException extends BaseException{

    public EmployeeException(String message, HttpStatus httpStatus, String errorCode){
        super(message, httpStatus, errorCode);
    }

}
