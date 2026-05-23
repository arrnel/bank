package com.bank.payment.ex;

public class OperationHandlerNotFoundException extends RuntimeException {
    public OperationHandlerNotFoundException(String message) {
        super(message);
    }
}
