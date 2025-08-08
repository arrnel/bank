package com.arrnel.payment.service.handler.impl;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.mapper.CurrencyWalletMapper;
import com.arrnel.payment.model.dto.CreateCurrencyWalletRequestDTO;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.service.BankAccountService;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.service.handler.OperationHandler;
import com.arrnel.payment.util.JsonConverter;
import com.arrnel.payment.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.arrnel.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateCurrencyWalletHandler implements OperationHandler {

    private final BankAccountService bankAccountService;
    private final CurrencyWalletMapper currencyWalletMapper;
    private final CurrencyWalletService currencyWalletService;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    @Transactional
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateCurrencyWalletRequestDTO.class);
        validationService.validate(
                requestId,
                OperationType.CREATE_CURRENCY_WALLET,
                request,
                CreateCurrencyWalletRequestDTO.class.getSimpleName()
        );

        var bankAccount = bankAccountService.findById(request.bankAccountId()).orElse(null);
        var currencyWallet = currencyWalletMapper.toEntity(request, bankAccount);

        var response = currencyWalletMapper.toCreateResponseDTO(
                currencyWalletService.save(currencyWallet),
                SUCCESS
        );

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                jsonConverter.convertToJson(response),
                OperationType.CREATE_CURRENCY_WALLET,
                SUCCESS
        );

    }

}
