package com.arrnel.payment.data.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum PaymentType {
    TRANSFER, DEPOSIT, WITHDRAWAL;

    public static Optional<PaymentType> fromString(String value) {
        return Stream.of(values())
                .filter(c -> c.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
