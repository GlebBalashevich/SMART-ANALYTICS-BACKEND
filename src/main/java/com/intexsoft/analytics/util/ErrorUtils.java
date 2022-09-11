package com.intexsoft.analytics.util;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ErrorUtils {

    private static final String INTERNAL_SERVER_ERROR_CODE = "ISE-0";

    private static final Map<HttpStatus, String> COMMON_ERRORS;

    static {
        COMMON_ERRORS = new EnumMap<>(HttpStatus.class);
        COMMON_ERRORS.put(HttpStatus.BAD_REQUEST, "BRE-0");
        COMMON_ERRORS.put(HttpStatus.FORBIDDEN, "FRE-0");
    }

    public static String defineCode(HttpStatus status) {
        return COMMON_ERRORS.getOrDefault(status, INTERNAL_SERVER_ERROR_CODE);
    }

    private ErrorUtils() {
    }
}
