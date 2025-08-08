package com.arrnel.payment.controller.rest;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.model.dto.*;
import com.arrnel.payment.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.arrnel.payment.model.enums.OperationType.*;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping({"/internal/payment/rest", "/internal/payment/rest/"})
@RequiredArgsConstructor
public class KafkaTestController {

    private static final String OPERATION_TOPIC = "operation-topic";
    private static final String OPERATION_TOPIC_RESULT = "operation-result-topic";

    private final OperationResultProducer operationResultProducer;
    private final JsonConverter jsonConverter;

    @PostMapping("/user/bank_account")
    public ResponseEntity<Void> createNewBankAccount(@RequestBody CreateBankAccountRequestDTO requestDTO) {
        var requestId = UUID.randomUUID().toString();
        operationResultProducer.produceResult(
                OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestDTO),
                CREATE_BANK_ACCOUNT
        );
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/user/currency_wallet")
    public ResponseEntity<Void> createCurrencyWallet(@RequestBody CreateCurrencyWalletRequestDTO requestDTO) {
        var requestId = UUID.randomUUID().toString();
        operationResultProducer.produceResult(
                OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestDTO),
                CREATE_CURRENCY_WALLET
        );
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/deposit")
    @ResponseStatus(CREATED)
    public ResponseEntity<Void> depositTransaction(@RequestBody CreateDepositRequestDTO requestDTO) {
        var requestId = UUID.randomUUID().toString();
        operationResultProducer.produceResult(
                OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestDTO),
                DEPOSIT
        );
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/transfer")
    @ResponseStatus(CREATED)
    public ResponseEntity<Void> depositTransaction(@RequestBody CreateTransferRequestDTO requestDTO) {
        var requestId = UUID.randomUUID().toString();
        operationResultProducer.produceResult(
                OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestDTO),
                TRANSFER
        );
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/refund")
    @ResponseStatus(CREATED)
    public ResponseEntity<Void> refundTransaction(@RequestBody CreateRefundRequestDTO requestDTO) {
        var requestId = UUID.randomUUID().toString();
        operationResultProducer.produceResult(
                OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestDTO),
                REFUND
        );
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/withdrawal")
    @ResponseStatus(CREATED)
    public ResponseEntity<Void> withdrawalTransaction(@RequestBody CreateWithdrawalRequestDTO requestDTO) {
        var requestId = UUID.randomUUID().toString();
        operationResultProducer.produceResult(
                OPERATION_TOPIC,
                requestId,
                jsonConverter.convertToJson(requestDTO),
                WITHDRAWAL
        );
        return new ResponseEntity<>(CREATED);
    }

}
