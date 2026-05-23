package com.bank.payment.service.handler.impl;

import com.bank.payment.controller.kafka.OperationResultProducer;
import com.bank.payment.model.dto.CreateBankAccountRequestDTO;
import com.bank.payment.model.enums.OperationType;
import com.bank.payment.service.BankAccountService;
import com.bank.payment.service.handler.OperationHandler;
import com.bank.payment.util.JsonConverter;
import com.bank.payment.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.bank.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateBankAccountHandler implements OperationHandler {

    private final BankAccountService bankAccountService;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateBankAccountRequestDTO.class);
        validationService.validate(
                request,
                CreateBankAccountRequestDTO.class.getSimpleName()
        );

        var response = bankAccountService.create(request);

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                OperationType.CREATE_BANK_ACCOUNT,
                SUCCESS,
                jsonConverter.convertToJson(response)
        );
    }
}