package com.arrnel.gateway.ex.handler;

import com.arrnel.gateway.ex.IllegalPaymentOperationException;
import com.arrnel.gateway.model.dto.ApiErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.ParametersAreNonnullByDefault;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ParametersAreNonnullByDefault
public class IllegalPaymentOperationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalPaymentOperationException.class)
    public ResponseEntity<ApiErrorDTO> handleIllegalOperationException(final IllegalPaymentOperationException ex) {
        log.error(
                "Payment service returned response with errors for type = [{}], request_id = [{}]",
                ex.getOperationType(),
                ex.getRequestId(),
                ex
        );

        return new ResponseEntity<>(ex.getApiError(), HttpStatus.BAD_REQUEST);
    }

}
