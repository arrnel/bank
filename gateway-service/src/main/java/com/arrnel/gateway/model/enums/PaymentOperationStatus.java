package com.arrnel.gateway.model.enums;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

public enum PaymentOperationStatus {
    CREATED, PROCESSING, SUCCESS, FAILED;

    @Nonnull
    public static Optional<PaymentOperationStatus> fromString(String value) {
        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
