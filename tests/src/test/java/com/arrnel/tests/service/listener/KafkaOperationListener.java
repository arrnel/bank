package com.arrnel.tests.service.listener;

import com.arrnel.tests.model.dto.*;
import com.arrnel.tests.model.enums.OperationType;
import com.arrnel.tests.service.KafkaStore;
import com.arrnel.tests.util.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static com.arrnel.tests.model.enums.OperationRecordHeaders.OPERATION_TYPE;

@Slf4j
public class KafkaOperationListener extends KafkaListener {

    private final JsonConverter jsonConverter;

    public KafkaOperationListener(Consumer<String, String> kafkaConsumer,
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
        if (operationTypeHeader == null) {
            throw new IllegalArgumentException("Header '%s' should be provided".formatted(OPERATION_TYPE.toString()));
        }
        var operationTypeValue = new String(operationTypeHeader.value());
        var operationType = OperationType.fromString(operationTypeValue);

        switch (operationType) {
            case CREATE_BANK_ACCOUNT -> KafkaStore.INSTANCE.saveBankAccountRequest(
                    requestId,
                    jsonConverter.convertToObj(message, CreateBankAccountRequestDTO.class)
            );
            case CREATE_CURRENCY_WALLET -> KafkaStore.INSTANCE.saveCurrencyWalletRequest(
                    requestId,
                    jsonConverter.convertToObj(message, CreateCurrencyWalletRequestDTO.class)
            );
            case DEPOSIT -> KafkaStore.INSTANCE.saveDepositRequest(
                    requestId,
                    jsonConverter.convertToObj(message, CreateDepositRequestDTO.class)
            );
            case TRANSFER -> KafkaStore.INSTANCE.saveTransferRequest(
                    requestId,
                    jsonConverter.convertToObj(message, CreateTransferRequestDTO.class)
            );
            case REFUND -> KafkaStore.INSTANCE.saveRefundRequest(
                    requestId,
                    jsonConverter.convertToObj(message, CreateRefundRequestDTO.class)
            );
            case WITHDRAWAL -> KafkaStore.INSTANCE.saveWithdrawalRequest(
                    requestId,
                    jsonConverter.convertToObj(message, CreateWithdrawalRequestDTO.class)
            );
            default -> throw new IllegalArgumentException("Unknown operation type: " + operationTypeValue);
        }

    }

}
