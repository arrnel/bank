package com.arrnel.payment.ex;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class OperationProcessingException extends RuntimeException {

    public OperationProcessingException(String message) {
        super(message);
    }

    public OperationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
