package com.bank.gateway.ex;

public class InternalServiceNotFoundException extends RuntimeException {
    public InternalServiceNotFoundException(String message) {
        super(message);
    }
}
