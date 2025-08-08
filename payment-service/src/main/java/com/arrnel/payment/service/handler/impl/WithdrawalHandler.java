package com.arrnel.payment.service.handler.impl;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.mapper.PaymentMapper;
import com.arrnel.payment.model.dto.CreateWithdrawalRequestDTO;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.service.PaymentService;
import com.arrnel.payment.service.handler.OperationHandler;
import com.arrnel.payment.util.JsonConverter;
import com.arrnel.payment.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.arrnel.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalHandler implements OperationHandler {

    private final CurrencyWalletService currencyWalletService;
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    @Transactional
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateWithdrawalRequestDTO.class);
        validationService.validate(
                requestId,
                OperationType.WITHDRAWAL,
                request,
                CreateWithdrawalRequestDTO.class.getSimpleName()
        );

        var currencyWallet = currencyWalletService.findById(request.currencyWalletId()).get();
        var deposit = paymentMapper.toEntity(request, currencyWallet)
                .setStatus(SUCCESS);

        var response = paymentMapper.toCreateResponseDTO(
                paymentService.save(deposit)
        );

        withdraw(currencyWallet, request.amount());

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                jsonConverter.convertToJson(response),
                OperationType.WITHDRAWAL,
                SUCCESS
        );
    }

    private void withdraw(CurrencyWalletEntity currencyWallet, BigDecimal amount) {
        currencyWallet.setBalance(currencyWallet.getBalance().subtract(amount));
        currencyWalletService.save(currencyWallet);
    }

}
