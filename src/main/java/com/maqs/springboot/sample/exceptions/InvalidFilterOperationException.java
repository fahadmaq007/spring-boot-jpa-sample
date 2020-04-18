package com.maqs.springboot.sample.exceptions;

/**
 * Invalid filter operation as per {@link com.maqs.springboot.sample.dto.SearchCriteria.Operation}
 */
public class InvalidFilterOperationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidFilterOperationException(String message) {
        super(message);
    }

    public InvalidFilterOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFilterOperationException(Throwable cause) {
        this(cause.getMessage(), cause);
    }
}
