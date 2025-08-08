package com.arrnel.payment.ex.handler;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.data.enums.OperationStatus;
import com.arrnel.payment.ex.IllegalOperationException;
import com.arrnel.payment.model.dto.ApiErrorDTO;
import com.arrnel.payment.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;


@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class IllegalOperationExceptionHandler {

    private static final String INVALID_OPERATION_REASON = "validation.exception.handler.invalid_operation.reason";
    private static final String INVALID_OPERATION_MESSAGE = "validation.exception.handler.invalid_operation.message";

    private final MessageSource messageSource;
    private final JsonConverter jsonConverter;
    private final OperationResultProducer operationResultProducer;

    @Value("${app.api.version}")
    private String apiVersion;

    @ExceptionHandler(IllegalOperationException.class)
    public void handleIllegalOperationException(final IllegalOperationException ex,
                                                final Locale locale
    ) {

        log.error("Unable to do [{}] operation with requestId = [{}]. Request contains illegal operation",
                ex.getOperationType(),
                ex.getRequestId());

        final String reason = messageSource.getMessage(
                INVALID_OPERATION_REASON,
                new Object[0],
                "Illegal operation",
                locale);

        final String message = messageSource.getMessage(
                INVALID_OPERATION_MESSAGE,
                new Object[0],
                "Illegal operation with type = [%s] and requestId = [%s]".formatted(ex.getOperationType(), ex.getRequestId()),
                locale);

        final var apiError = ApiErrorDTO.builder()
                .apiVersion(apiVersion)
                .code(HttpStatus.NOT_FOUND.toString())
                .domain("")
                .reason(reason)
                .message(message)
                .build();

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                ex.getRequestId(),
                jsonConverter.convertToJson(apiError),
                ex.getOperationType(),
                OperationStatus.FAILED
        );

    }

}
