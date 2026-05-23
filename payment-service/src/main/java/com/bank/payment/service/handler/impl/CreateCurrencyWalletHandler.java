package com.bank.payment.service.handler.impl;

import com.bank.payment.controller.kafka.OperationResultProducer;
import com.bank.payment.model.dto.CreateCurrencyWalletRequestDTO;
import com.bank.payment.model.enums.OperationType;
import com.bank.payment.service.CurrencyWalletService;
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
public class CreateCurrencyWalletHandler implements OperationHandler {

    private final CurrencyWalletService currencyWalletService;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateCurrencyWalletRequestDTO.class);
        validationService.validate(
                request,
                CreateCurrencyWalletRequestDTO.class.getSimpleName()
        );

        var response = currencyWalletService.create(request);

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                OperationType.CREATE_CURRENCY_WALLET,
                SUCCESS,
                jsonConverter.convertToJson(response)
        );

    }

}
