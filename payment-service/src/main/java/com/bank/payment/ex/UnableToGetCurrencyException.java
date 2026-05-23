package com.bank.payment.ex;

import com.bank.payment.data.enums.Currency;

public class UnableToGetCurrencyException extends RuntimeException {

    private final Currency from;
    private final Currency to;

    public UnableToGetCurrencyException(Currency from, Currency to, Throwable cause) {
        super("Unable to get currency rate: %s -> %s".formatted(from, to), cause);
        this.from = from;
        this.to = to;
    }

    public UnableToGetCurrencyException(Currency from, Currency to) {
        super("Unable to get currency rate: %s -> %s".formatted(from, to));
        this.from = from;
        this.to = to;
    }
}
