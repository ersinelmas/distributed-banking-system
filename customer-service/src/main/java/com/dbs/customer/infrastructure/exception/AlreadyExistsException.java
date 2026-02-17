package com.dbs.customer.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BusinessException {
    public AlreadyExistsException(String message) {
        super(message, "ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}