package com.intexsoft.analytics.controller.advice;

import java.time.Instant;

import com.intexsoft.analytics.dto.error.BadRequestErrorResponse;
import com.intexsoft.analytics.dto.error.BaseErrorResponse;
import com.intexsoft.analytics.exception.BaseException;
import com.intexsoft.analytics.util.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error occurred, contact server administrator";

    private static final String BAD_REQUEST_MESSAGE = "Invalid request parameters";

    @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseErrorResponse> handle(BaseException e, ServerWebExchange serverWebExchange) {
        final var httpStatus = e.getHttpStatus();
        final var errorCode = e.getErrorCode();
        final var message = e.getMessage();
        return new ResponseEntity<>(buildBaseErrorResponse(serverWebExchange, errorCode, message, httpStatus),
                httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<BadRequestErrorResponse> handle(WebExchangeBindException e,
            ServerWebExchange serverWebExchange) {
        final var httpStatus = HttpStatus.BAD_REQUEST;
        final var errorCode = ErrorUtils.defineCode(httpStatus);
        return new ResponseEntity<>(
                buildBadRequestErrorResponse(serverWebExchange, errorCode, httpStatus, e),
                httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<BaseErrorResponse> handle(ServerWebInputException e, ServerWebExchange serverWebExchange) {
        final var httpStatus = HttpStatus.BAD_REQUEST;
        final var errorCode = ErrorUtils.defineCode(httpStatus);
        final var message = e.getReason();
        return new ResponseEntity<>(buildBaseErrorResponse(serverWebExchange, errorCode, message, httpStatus),
                httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseErrorResponse> handle(Throwable e, ServerWebExchange serverWebExchange) {
        log.error("Error occurred while processing request", e);
        final var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final var errorCode = ErrorUtils.defineCode(httpStatus);
        return new ResponseEntity<>(buildBaseErrorResponse(serverWebExchange, errorCode,
                INTERNAL_SERVER_ERROR_MESSAGE, httpStatus), httpStatus);
    }

    private BaseErrorResponse buildBaseErrorResponse(ServerWebExchange exchange, String code, String message,
            HttpStatus status) {
        return BaseErrorResponse.builder()
                .timestamp(Instant.now())
                .error(status.getReasonPhrase())
                .code(code)
                .path(exchange.getRequest().getPath().value())
                .message(message)
                .build();
    }

    private BadRequestErrorResponse buildBadRequestErrorResponse(ServerWebExchange exchange, String code,
            HttpStatus status, WebExchangeBindException e) {
        final var validationDetails = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> BadRequestErrorResponse.ValidationDetail.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();
        return BadRequestErrorResponse.builder()
                .timestamp(Instant.now())
                .error(status.getReasonPhrase())
                .code(code)
                .path(exchange.getRequest().getPath().value())
                .message(BAD_REQUEST_MESSAGE)
                .details(validationDetails)
                .build();
    }

}
