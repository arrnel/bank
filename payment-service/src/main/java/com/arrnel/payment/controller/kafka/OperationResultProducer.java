package com.arrnel.payment.controller.kafka;

import com.arrnel.payment.data.enums.OperationStatus;
import com.arrnel.payment.model.enums.OperationType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;

@Slf4j
@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class OperationResultProducer {

    public static final String OPERATION_RESULT_TOPIC = "operation-result-topic";
    protected final KafkaTemplate<String, String> kafkaTemplate;

    public void produceResult(String topic,
                              String requestId,
                              String message,
                              OperationType operation
    ) {
        var kafkaMessage = buildMessage(topic, requestId, message, operation, null);
        kafkaTemplate.send(kafkaMessage);
        log.info("Successfully send {} transaction result: {}", operation.name().toLowerCase(), kafkaMessage);
    }

    public void produceResult(String topic,
                              String requestId,
                              String message,
                              OperationType operation,
                              OperationStatus operationStatus
    ) {
        var kafkaMessage = buildMessage(topic, requestId, message, operation, operationStatus);
        kafkaTemplate.send(kafkaMessage);
        log.info("Successfully send {} transaction result: {}", operation.name().toLowerCase(), kafkaMessage);
    }

    @Nonnull
    private Message<String> buildMessage(String topic,
                                         String requestId,
                                         String message,
                                         OperationType operation,
                                         @Nullable OperationStatus status
    ) {
        var messageBuilder = MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, requestId)
                .setHeader("operation_type", operation.toString());
        if (status != null)
            messageBuilder.setHeader("operation_status", status.toString());
        return messageBuilder.build();
    }

}
