package com.arrnel.payment.data.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum OperationStatus {
    CREATED, PROCESSING, SUCCESS, FAILED;

    public static Optional<OperationStatus> fromString(String value) {
        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
