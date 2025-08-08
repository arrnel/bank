package com.arrnel.payment.ex;

import com.arrnel.payment.model.enums.OperationType;
import lombok.Getter;
import org.springframework.validation.FieldError;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
public class IllegalOperationException extends RuntimeException {

    private final List<FieldError> errors;
    private final String requestId;
    private final OperationType operationType;

    public IllegalOperationException(String requestId, OperationType operationType, List<FieldError> fieldErrors) {
        super("Bad request. Contains errors: " + fieldErrors);
        this.requestId = requestId;
        this.operationType = operationType;
        this.errors = fieldErrors;
    }

}
