package com.example.buyingCurrencyService.handlers.exception;

public class NoConnectionWithServiceException extends RuntimeException{

    public NoConnectionWithServiceException() {
    }

    public NoConnectionWithServiceException(String message) {
        super(message);
    }

    public NoConnectionWithServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoConnectionWithServiceException(Throwable cause) {
        super(cause);
    }
}