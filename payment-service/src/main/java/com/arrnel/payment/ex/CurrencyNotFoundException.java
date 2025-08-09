package com.arrnel.payment.ex;

import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
public class CurrencyNotFoundException extends RuntimeException {

    private final String currency;

    public CurrencyNotFoundException(String currency) {
        super("Currency [%s] not found".formatted(currency));
        this.currency = currency;
    }

}
