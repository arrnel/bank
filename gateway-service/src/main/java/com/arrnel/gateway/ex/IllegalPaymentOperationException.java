package com.arrnel.gateway.ex;

import com.arrnel.gateway.model.dto.ApiErrorDTO;
import com.arrnel.gateway.model.enums.PaymentOperationType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class IllegalPaymentOperationException extends RuntimeException {

    private final UUID requestId;
    private final PaymentOperationType operationType;
    private final ApiErrorDTO apiError;

    public IllegalPaymentOperationException(UUID requestId, PaymentOperationType operationType, ApiErrorDTO apiError) {
        super("Payment Service returned response an with illegal payment operation for request: %s".formatted(requestId));
        this.requestId = requestId;
        this.operationType = operationType;
        this.apiError = apiError;
    }

}
