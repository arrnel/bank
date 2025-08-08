package com.arrnel.gateway.controller;

import com.arrnel.gateway.client.kafka.PaymentOperationProducer;
import com.arrnel.gateway.client.kafka.PaymentOperationResultConsumer;
import com.arrnel.gateway.model.dto.*;
import com.arrnel.gateway.model.enums.PaymentOperationType;
import com.arrnel.gateway.util.JsonConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping({"/api/v1/payment", "/api/v1/payment/"})
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentOperationProducer paymentProducer;
    private final PaymentOperationResultConsumer paymentConsumer;
    private final JsonConverter jsonConverter;
    private static final long TIMEOUT_SECONDS = 10;

    @PostMapping({"/bank_account", "/bank_account/"})
    @ResponseStatus(CREATED)
    public CreateOperationResponseDTO addNewBankAccount(@Valid @RequestBody final CreateBankAccountRequestDTO requestBody) {
        var requestId = UUID.randomUUID();
        paymentConsumer.registerOperation(requestId);
        paymentProducer.produceResult(
                PaymentOperationProducer.OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestBody),
                PaymentOperationType.CREATE_BANK_ACCOUNT
        );
        return paymentConsumer.waitForResult(requestId, TIMEOUT_SECONDS);
    }

    @PostMapping({"/currency_wallet", "/currency_wallet/"})
    @ResponseStatus(CREATED)
    public CreateOperationResponseDTO addNewCurrencyWallet(@Valid @RequestBody final CreateCurrencyWalletRequestDTO requestBody) {
        var requestId = UUID.randomUUID();
        paymentConsumer.registerOperation(requestId);
        paymentProducer.produceResult(
                PaymentOperationProducer.OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestBody),
                PaymentOperationType.CREATE_CURRENCY_WALLET
        );
        return paymentConsumer.waitForResult(requestId, TIMEOUT_SECONDS);
    }

    @PostMapping({"/deposit", "/deposit/"})
    @ResponseStatus(CREATED)
    public CreateOperationResponseDTO addNewDeposit(@Valid @RequestBody final CreateDepositRequestDTO requestBody) {
        var requestId = UUID.randomUUID();
        paymentConsumer.registerOperation(requestId);
        paymentProducer.produceResult(
                PaymentOperationProducer.OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestBody),
                PaymentOperationType.DEPOSIT
        );
        return paymentConsumer.waitForResult(requestId, TIMEOUT_SECONDS);
    }

    @PostMapping({"/transfer", "/transfer/"})
    @ResponseStatus(CREATED)
    public CreateOperationResponseDTO addNewTransfer(@Valid @RequestBody final CreateTransferRequestDTO requestBody) {
        var requestId = UUID.randomUUID();
        paymentConsumer.registerOperation(requestId);
        paymentProducer.produceResult(
                PaymentOperationProducer.OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestBody),
                PaymentOperationType.TRANSFER
        );
        return paymentConsumer.waitForResult(requestId, TIMEOUT_SECONDS);
    }

    @PostMapping({"/refund", "/refund/"})
    @ResponseStatus(CREATED)
    public CreateOperationResponseDTO addNewRefund(@Valid @RequestBody final CreateRefundRequestDTO requestBody) {
        var requestId = UUID.randomUUID();
        paymentConsumer.registerOperation(requestId);
        paymentProducer.produceResult(
                PaymentOperationProducer.OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestBody),
                PaymentOperationType.REFUND
        );
        return paymentConsumer.waitForResult(requestId, TIMEOUT_SECONDS);
    }

    @PostMapping({"/withdrawal", "/withdrawal/"})
    @ResponseStatus(CREATED)
    public CreateOperationResponseDTO addNewWithdrawal(@Valid @RequestBody final CreateWithdrawalRequestDTO requestBody) {
        var requestId = UUID.randomUUID();
        paymentConsumer.registerOperation(requestId);
        paymentProducer.produceResult(
                PaymentOperationProducer.OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestBody),
                PaymentOperationType.WITHDRAWAL
        );
        return paymentConsumer.waitForResult(requestId, TIMEOUT_SECONDS);
    }

}
