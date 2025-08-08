package com.arrnel.payment.data.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum Currency {
    USD, EUR, GBP, RUB, CAD, KZT, JPY, CNY;

    public static Optional<Currency> fromString(String value) {
        return Stream.of(values())
                .filter(c -> c.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
