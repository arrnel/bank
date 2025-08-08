package com.arrnel.gateway.model.enums;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

public enum Currency {
    USD, EUR, GBP, RUB, CAD, KZT, JPY, CNY;

    @Nonnull
    public static Optional<Currency> fromString(String value) {
        return Stream.of(values())
                .filter(c -> c.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
