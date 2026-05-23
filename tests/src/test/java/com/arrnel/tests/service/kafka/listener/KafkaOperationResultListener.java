package com.bank.tests.service.kafka.listener;

import com.bank.tests.model.dto.ApiErrorDTO;
import com.bank.tests.model.dto.payment.CreateOperationResponseDTO;
import com.bank.tests.model.enums.OperationStatus;
import com.bank.tests.service.kafka.KafkaStore;
import com.bank.tests.util.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static com.bank.tests.model.enums.OperationRecordHeaders.OPERATION_STATUS;
import static com.bank.tests.model.enums.OperationRecordHeaders.OPERATION_TYPE;

@Slf4j
public class KafkaOperationResultListener extends KafkaListener {

    private final JsonConverter jsonConverter;

    public KafkaOperationResultListener(Consumer<String, String> kafkaConsumer,
                                        JsonConverter jsonConverter,
                                        String topic
    ) {
        super(kafkaConsumer, topic);
        this.jsonConverter = jsonConverter;
    }

    protected void process(ConsumerRecord<String, String> consumerRecord) {
        var requestId = consumerRecord.key();
        var message = consumerRecord.value();

        var operationTypeHeader = consumerRecord.headers().lastHeader(OPERATION_TYPE.toString());
        var statusHeader = consumerRecord.headers().lastHeader(OPERATION_STATUS.toString());

        if (operationTypeHeader == null || statusHeader == null)
            throw new IllegalArgumentException("Headers '%s' and '%s' should be provided"
                    .formatted(OPERATION_TYPE.toString(), OPERATION_STATUS.toString()));

        var operationStatusVal = new String(statusHeader.value());
        var operationStatus = OperationStatus.fromString(operationStatusVal);

        var clazz = switch (operationStatus) {
            case SUCCESS -> CreateOperationResponseDTO.class;
            case FAILED -> ApiErrorDTO.class;
            default -> throw new IllegalArgumentException("Unknown operation status: %s".formatted(operationStatusVal));
        };
        KafkaStore.INSTANCE.saveOperationResponse(requestId, jsonConverter.convertToObj(message, clazz));
    }

}
