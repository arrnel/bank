package com.arrnel.payment.ex.handler;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.ex.CurrencyNotFoundException;
import com.arrnel.payment.ex.IllegalOperationException;
import com.arrnel.payment.ex.OperationProcessingException;
import com.arrnel.payment.model.dto.ApiErrorDTO;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.util.JsonConverter;
import com.arrnel.payment.validation.ValidationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class OperationExceptionHandler implements CommonErrorHandler {

    private final MessageSource messageSource;
    private final JsonConverter jsonConverter;
    private final OperationResultProducer operationResultProducer;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Value("${app.api.version}")
    private String apiVersion;

    public void handleException(Exception ex, String requestId, OperationType operationType) {
        var apiError = switch (ex) {
            case IllegalOperationException illegalOperationEx ->
                    handleIllegalOperationException(requestId, operationType, illegalOperationEx);
            case CurrencyNotFoundException currencyNotFoundEx ->
                    handleCurrencyNotFoundException(requestId, operationType, currencyNotFoundEx);
            case OperationProcessingException operationProcessingEx ->
                    handleOperationProcessingException(requestId, operationType, operationProcessingEx);
            default -> handleGeneralException(requestId, operationType, ex);
        };
        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                operationType,
                jsonConverter.convertToJson(apiError)
        );
    }

    private ApiErrorDTO handleIllegalOperationException(String requestId,
                                                        OperationType operationType,
                                                        IllegalOperationException ex
    ) {
        log.error("Unable to do [{}] operation with requestId = [{}]. Request contains illegal operation",
                operationType, requestId);

        final var message = messageSource.getMessage(
                ValidationCode.ILLEGAL_OPERATION_MESSAGE_CODE,
                new Object[0],
                "Illegal [%s] operation with id [%s]".formatted(operationType.name(), requestId),
                locale
        );

        final var errorItems = ex.getErrors().stream()
                .map(error -> new ApiErrorDTO.ErrorItem(
                        "", // URI недоступен в Kafka context
                        error.getField(),
                        "[%s] %s".formatted(error.getCode(), error.getDefaultMessage())))
                .toList();

        return ApiErrorDTO.builderErrors()
                .apiVersion(apiVersion)
                .code(HttpStatus.BAD_REQUEST.toString())
                .message(message)
                .errorItems(errorItems)
                .buildErrors();
    }

    private ApiErrorDTO handleCurrencyNotFoundException(String requestId,
                                                        OperationType operationType,
                                                        CurrencyNotFoundException ex
    ) {
        log.error("Currency [{}] not found for operation {} with requestId {}",
                ex.getCurrency(), operationType, requestId);

        var message = messageSource.getMessage(
                ValidationCode.CURRENCY_NOT_FOUND_MESSAGE,
                new String[]{ex.getCurrency()},
                "Currency not found",
                locale
        );

        var reason = messageSource.getMessage(
                ValidationCode.CURRENCY_NOT_FOUND_REASON,
                new String[]{ex.getCurrency()},
                "Currency [%s] not found".formatted(ex.getCurrency()),
                locale
        );

        return ApiErrorDTO.builder()
                .apiVersion(apiVersion)
                .code(HttpStatus.NOT_FOUND.toString())
                .message(message)
                .reason(reason)
                .build();
    }

    private ApiErrorDTO handleOperationProcessingException(String requestId,
                                                           OperationType operationType,
                                                           OperationProcessingException ex
    ) {
        log.error("Operation processing error for {} with requestId {}: {}",
                operationType, requestId, ex.getMessage());

        var message = messageSource.getMessage(
                ValidationCode.OPERATION_PROCESSING_MESSAGE,
                new String[]{operationType.name(), requestId},
                "Unable to process operation",
                locale
        );

        var reason = messageSource.getMessage(
                ValidationCode.OPERATION_PROCESSING_REASON,
                new String[]{operationType.name(), requestId, ex.getMessage()},
                "Unable to process operation [%s] with requestId {%s}. Got error message: {%s}",
                locale
        );


        return ApiErrorDTO.builder()
                .apiVersion(apiVersion)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message(message)
                .reason(reason)
                .build();
    }

    private ApiErrorDTO handleGeneralException(String requestId,
                                               OperationType operationType,
                                               Exception ex
    ) {
        log.error("Unexpected error for operation {} with requestId {}", operationType, requestId, ex);

        var message = messageSource.getMessage(
                ValidationCode.GENERAL_EXCEPTION_MESSAGE,
                new Object[0],
                "Unknown exception",
                locale
        );

        var reason = ex.getMessage();

        return ApiErrorDTO.builder()
                .apiVersion(apiVersion)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message(message)
                .reason(reason)
                .build();
    }

}
