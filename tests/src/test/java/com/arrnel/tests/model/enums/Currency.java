package com.arrnel.tests.model.enums;

import java.util.stream.Stream;

public enum Currency {
    USD, EUR, GBP, RUB, CAD, KZT, JPY, CNY, UNKNOWN;

    public static Currency fromString(String value) {
        return Stream.of(values())
                .filter(c -> c.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
