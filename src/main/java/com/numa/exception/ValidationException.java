package com.numa.exception;

/**
 * Exception thrown when validation fails.
 * Typically results in HTTP 400 Bad Request response.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
