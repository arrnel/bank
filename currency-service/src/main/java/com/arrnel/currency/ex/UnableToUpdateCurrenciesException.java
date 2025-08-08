package com.arrnel.currency.ex;

public class UnableToUpdateCurrenciesException extends RuntimeException {
    public UnableToUpdateCurrenciesException(String message) {
        super(message);
    }

    public UnableToUpdateCurrenciesException(String message, Throwable cause) {
        super(message, cause);
    }
}
