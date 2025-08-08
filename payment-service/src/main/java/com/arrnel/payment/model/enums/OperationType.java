package com.arrnel.payment.model.enums;

import java.util.stream.Stream;

public enum OperationType {
    CREATE_BANK_ACCOUNT,
    CREATE_CURRENCY_WALLET,
    DEPOSIT,
    TRANSFER,
    WITHDRAWAL,
    REFUND,
    DELETE_CURRENCY_WALLET,
    DELETE_BANK_ACCOUNT,
    UNKNOWN;

    public static OperationType fromString(String value) {
        return Stream.of(values())
                .filter(o -> o.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
