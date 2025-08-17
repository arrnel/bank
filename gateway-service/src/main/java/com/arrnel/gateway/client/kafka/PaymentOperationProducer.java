package com.arrnel.gateway.client.kafka;

import com.arrnel.gateway.model.enums.PaymentOperationType;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class PaymentOperationProducer {

    public static final String OPERATION_TOPIC = "operation-topic";
    protected final KafkaTemplate<String, String> kafkaTemplate;

    public void produceResult(String topic,
                              UUID requestId,
                              String message,
                              PaymentOperationType operation
    ) {
        var kafkaMessage = buildMessage(topic, requestId, message, operation);
        kafkaTemplate.send(kafkaMessage);
        log.info("Successfully send {} transaction: {}", operation.name().toLowerCase(), kafkaMessage);
    }

    @Nonnull
    protected Message<String> buildMessage(String topic,
                                           UUID requestId,
                                           String message,
                                           PaymentOperationType operation
    ) {
        return MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, requestId.toString())
                .setHeader("operation_type", operation.toString())
                .build();
    }

}
