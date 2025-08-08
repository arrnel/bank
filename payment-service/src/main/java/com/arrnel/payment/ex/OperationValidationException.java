package com.arrnel.payment.ex;

import com.arrnel.payment.model.enums.OperationType;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.List;

@Getter
public class OperationValidationException extends RuntimeException {
    private final OperationType operationType;
    private final List<String> errors;

    public OperationValidationException(@Nonnull OperationType operationType, @Nonnull List<String> errors) {
        super("%s has validation errors: %s".formatted(operationType.name(), errors.toString()));
        this.operationType = operationType;
        this.errors = errors;
    }
}
