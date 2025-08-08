package com.arrnel.payment.controller.kafka;

import com.arrnel.payment.ex.OperationHandlerNotFoundException;
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
public class OperationConsumer {

    private final Map<OperationType, OperationHandler> operationHandlers;

    @KafkaListener(topics = "operation-topic", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOperation(final ConsumerRecord<String, String> kafkaRecord) {

        var operation = getPaymentTransactionOperation(kafkaRecord);
        var handler = operationHandlers.get(operation);

        if (handler == null)
            throw new OperationHandlerNotFoundException(
                    "Unknown operation: %s. RECORD: %s".formatted(operation, kafkaRecord)
            );

        handler.process(
                kafkaRecord.key(),
                kafkaRecord.value()
        );

    }

    @Nonnull
    private OperationType getPaymentTransactionOperation(final ConsumerRecord<String, String> kafkaRecord) {
        var commandHeader = kafkaRecord.headers().lastHeader("operation_type");
        return commandHeader != null
                ? OperationType.fromString(new String(commandHeader.value()))
                : OperationType.UNKNOWN;
    }

}
