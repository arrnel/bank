package com.arrnel.tests.model.enums;

import java.util.stream.Stream;

public enum OperationStatus {
    SUCCESS, FAILED, UNKNOWN;

    public static OperationStatus fromString(String value) {
        return Stream.of(values())
                .filter(o -> o.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
