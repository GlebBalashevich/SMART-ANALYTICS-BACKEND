package com.intexsoft.analytics.dto.error;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseErrorResponse {

    private Instant timestamp;

    private String error;

    private String code;

    private String message;

    private String path;

}
