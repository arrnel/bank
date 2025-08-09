package com.arrnel.payment.validation;

import com.arrnel.payment.ex.IllegalOperationException;
import com.arrnel.payment.ex.handler.OperationExceptionHandler;
import com.arrnel.payment.model.enums.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import javax.annotation.ParametersAreNonnullByDefault;

@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class ValidationService {

    private final Validator validator;
    private final OperationExceptionHandler operationExceptionHandler;

    public void validate(String requestId, OperationType operationType, Object object, String objectName) {

        final BindingResult bindingResult = new BeanPropertyBindingResult(object, objectName);
        validator.validate(object, bindingResult);

        if (bindingResult.hasErrors())
            operationExceptionHandler.handleException(
                    new IllegalOperationException(bindingResult.getFieldErrors()),
                    requestId,
                    operationType
            );
    }

}
