package com.arrnel.payment.controller.kafka;

import com.arrnel.payment.ex.OperationHandlerNotFoundException;
import com.arrnel.payment.ex.handler.OperationExceptionHandler;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.service.handler.OperationHandler;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class OperationListener {

    private final Map<OperationType, OperationHandler> operationHandlers;
    private final OperationExceptionHandler operationExceptionHandler;

    @KafkaListener(topics = "operation-topic", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOperation(final ConsumerRecord<String, String> kafkaRecord) {

        var operation = getPaymentOperation(kafkaRecord);
        var requestId = kafkaRecord.key();
        var handler = operationHandlers.get(operation);

        if (handler == null)
            throw new OperationHandlerNotFoundException(
                    "Unknown operation: %s. RECORD: %s".formatted(operation, kafkaRecord)
            );

        try {
            handler.process(
                    kafkaRecord.key(),
                    kafkaRecord.value()
            );
        } catch (Exception ex) {
            operationExceptionHandler.handleException(ex, requestId, operation);
        }

    }

    @Nonnull
    private OperationType getPaymentOperation(final ConsumerRecord<String, String> kafkaRecord) {
        var commandHeader = kafkaRecord.headers().lastHeader("operation_type");
        return commandHeader != null
                ? OperationType.fromString(new String(commandHeader.value()))
                : OperationType.UNKNOWN;
    }

}
