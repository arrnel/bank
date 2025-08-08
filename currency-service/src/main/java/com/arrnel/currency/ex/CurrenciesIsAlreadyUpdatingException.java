package com.arrnel.currency.ex;

public class CurrenciesIsAlreadyUpdatingException extends RuntimeException {
    public CurrenciesIsAlreadyUpdatingException(String message) {
        super(message);
    }

    public CurrenciesIsAlreadyUpdatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
