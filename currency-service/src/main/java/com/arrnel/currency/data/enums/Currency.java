package com.arrnel.currency.data.enums;

import com.arrnel.currency.ex.CurrencyNotFoundException;

import java.util.stream.Stream;

public enum Currency {
    USD, EUR, GBP, RUB, CAD, KZT, JPY, CNY;

    public static final Currency BASE_CURRENCY = Currency.USD;

    public static Currency[] getCurrenciesWithoutBaseCurrency() {
        return Stream.of(values())
                .filter(currency -> currency.equals(BASE_CURRENCY))
                .toArray(Currency[]::new);
    }

    public static Currency fromString(String value) {
        return Stream.of(values())
                .filter(c -> c.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CurrencyNotFoundException(value));
    }
}
