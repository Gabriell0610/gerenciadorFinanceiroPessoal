package dev.vieira.ms_finance_api.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
