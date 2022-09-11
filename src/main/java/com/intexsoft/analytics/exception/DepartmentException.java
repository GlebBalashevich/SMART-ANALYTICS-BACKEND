package com.intexsoft.analytics.exception;

import org.springframework.http.HttpStatus;

public class DepartmentException extends BaseException{

    public DepartmentException(String message, HttpStatus httpStatus, String errorCode){
        super(message, httpStatus, errorCode);
    }

}
