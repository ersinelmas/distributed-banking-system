package com.dbs.customer.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String errorCode,
        String message,
        LocalDateTime timestamp,
        String path,
        Map<String, String> validationErrors
) {}