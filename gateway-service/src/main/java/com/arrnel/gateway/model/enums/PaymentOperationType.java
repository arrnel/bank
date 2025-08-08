package com.arrnel.gateway.model.enums;

import jakarta.annotation.Nonnull;

import java.util.stream.Stream;

public enum PaymentOperationType {
    CREATE_BANK_ACCOUNT,
    CREATE_CURRENCY_WALLET,
    DEPOSIT,
    TRANSFER,
    WITHDRAWAL,
    REFUND,
    DELETE_CURRENCY_WALLET,
    DELETE_BANK_ACCOUNT,
    UNKNOWN;

    @Nonnull
    public static PaymentOperationType fromString(String value) {
        return Stream.of(values())
                .filter(o -> o.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
