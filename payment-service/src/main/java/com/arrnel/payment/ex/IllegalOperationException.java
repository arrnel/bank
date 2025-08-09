package com.arrnel.payment.ex;

import lombok.Getter;
import org.springframework.validation.FieldError;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
public class IllegalOperationException extends RuntimeException {

    private final List<FieldError> errors;

    public IllegalOperationException(List<FieldError> fieldErrors) {
        super("Bad request. Contains errors: " + fieldErrors);
        this.errors = fieldErrors;
    }

}
