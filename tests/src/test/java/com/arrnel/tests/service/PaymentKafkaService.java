package com.arrnel.tests.service;

import com.arrnel.tests.model.dto.*;
import com.arrnel.tests.model.enums.OperationType;
import com.arrnel.tests.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.arrnel.tests.model.enums.OperationRecordHeaders.OPERATION_TYPE;
import static com.arrnel.tests.model.enums.OperationType.*;

@Slf4j
@RequiredArgsConstructor
public class PaymentKafkaService {

    private static final String OPERATION_TOPIC = "operation-topic";

    private final KafkaProducer kafkaProducer;
    private final KafkaStore kafkaStore;
    private final JsonConverter jsonConverter;

    @Nonnull
    public CreateOperationResponseDTO sendCreateBankAccountRequest(CreateBankAccountRequestDTO request) {
        return (CreateOperationResponseDTO) sendOperationRequest(CREATE_BANK_ACCOUNT, request);
    }

    @Nonnull
    public ApiErrorDTO sendCreateBankAccountRequestWithError(CreateBankAccountRequestDTO request) {
        return (ApiErrorDTO) sendOperationRequest(CREATE_BANK_ACCOUNT, request);
    }

    @Nonnull
    public CreateOperationResponseDTO sendCreateCurrencyWalletRequest(CreateCurrencyWalletRequestDTO request) {
        return (CreateOperationResponseDTO) sendOperationRequest(CREATE_CURRENCY_WALLET, request);
    }

    @Nonnull
    public ApiErrorDTO sendCreateCurrencyWalletRequestWithError(CreateCurrencyWalletRequestDTO request) {
        return (ApiErrorDTO) sendOperationRequest(CREATE_CURRENCY_WALLET, request);
    }

    @Nonnull
    public CreateOperationResponseDTO sendDepositRequest(CreateDepositRequestDTO request) {
        return (CreateOperationResponseDTO) sendOperationRequest(DEPOSIT, request);
    }

    @Nonnull
    public ApiErrorDTO sendDepositRequestWithError(CreateDepositRequestDTO request) {
        return (ApiErrorDTO) sendOperationRequest(DEPOSIT, request);
    }

    @Nonnull
    public CreateOperationResponseDTO sendTransferRequest(CreateTransferRequestDTO request) {
        return (CreateOperationResponseDTO) sendOperationRequest(TRANSFER, request);
    }

    @Nonnull
    public ApiErrorDTO sendTransferRequestWithError(CreateTransferRequestDTO request) {
        return (ApiErrorDTO) sendOperationRequest(TRANSFER, request);
    }

    @Nonnull
    public CreateOperationResponseDTO sendRefundRequest(CreateRefundRequestDTO request) {
        return (CreateOperationResponseDTO) sendOperationRequest(REFUND, request);
    }

    @Nonnull
    public ApiErrorDTO sendRefundRequestWithError(CreateRefundRequestDTO request) {
        return (ApiErrorDTO) sendOperationRequest(REFUND, request);
    }

    @Nonnull
    public CreateOperationResponseDTO sendWithdrawalRequest(CreateWithdrawalRequestDTO request) {
        return (CreateOperationResponseDTO) sendOperationRequest(WITHDRAWAL, request);
    }

    @Nonnull
    public ApiErrorDTO sendWithdrawalRequestWithError(CreateWithdrawalRequestDTO request) {
        return (ApiErrorDTO) sendOperationRequest(WITHDRAWAL, request);
    }

    @Nonnull
    private <T> Object sendOperationRequest(OperationType operationType, T request) {
        var requestId = UUID.randomUUID().toString();
        var headers = Map.of(OPERATION_TYPE.toString(), operationType.toString());
        var message = jsonConverter.convertToJson(request);

        log.info("Send [{}] request with id = [{}] and body: {}", operationType, requestId, message);
        kafkaProducer.sendMessage(
                OPERATION_TOPIC,
                requestId,
                headers,
                message
        );

        try {
            return Optional.ofNullable(
                            kafkaStore.getOperationResponse(requestId))
                    .orElseThrow(() ->
                            new RuntimeException("No operation response found for requestId: " + requestId)
                    );
        } catch (Exception ex) {
            log.error("Unable to get operation result", ex);
            throw new RuntimeException(ex);
        }
    }

}
