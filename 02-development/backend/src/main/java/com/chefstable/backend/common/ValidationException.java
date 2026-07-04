package com.chefstable.backend.common;

import org.springframework.http.HttpStatus;

public class ValidationException extends AppException {

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message, HttpStatus.BAD_REQUEST);
    }
}
