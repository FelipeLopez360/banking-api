package com.felipelopez.bank_service.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
