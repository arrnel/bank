package com.arrnel.gateway.ex;

public class InternalServiceNotFoundException extends RuntimeException {
    public InternalServiceNotFoundException(String message) {
        super(message);
    }
}
