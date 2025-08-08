package com.arrnel.currency.ex;

import com.arrnel.currency.data.enums.Currency;
import lombok.Getter;

@Getter
public class CurrencyNotFoundException extends RuntimeException {

    private final String currency;

    public CurrencyNotFoundException(String currency) {
        super("Currency [%s] not found".formatted(currency));
        this.currency = currency;
    }

    public CurrencyNotFoundException(Currency currency) {
        super("Currency [%s] not found".formatted(currency.name()));
        this.currency = currency.name();
    }

}
