package com.arrnel.payment.service.handler.impl;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.mapper.PaymentMapper;
import com.arrnel.payment.model.dto.CreateTransferRequestDTO;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.service.PaymentService;
import com.arrnel.payment.service.client.currency.CurrencyClientService;
import com.arrnel.payment.service.handler.OperationHandler;
import com.arrnel.payment.util.JsonConverter;
import com.arrnel.payment.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.arrnel.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferHandler implements OperationHandler {

    private final CurrencyWalletService currencyWalletService;
    private final CurrencyClientService currencyClientService;
    private final PaymentService paymentService;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateTransferRequestDTO.class);
        validationService.validate(
                requestId,
                OperationType.TRANSFER,
                request,
                CreateTransferRequestDTO.class.getSimpleName()
        );

        var currencyWallets = currencyWalletService.findSourceAndDestinationCw(request.sourceId(), request.destinationId());
        var sourceCw = currencyWallets.get(CurrencyWalletService.SOURCE_CURRENCY_WALLET);
        var destinationCw = currencyWallets.get(CurrencyWalletService.DESTINATION_CURRENCY_WALLET);

        var transfer = paymentMapper.toEntity(request, sourceCw, destinationCw)
                .setStatus(SUCCESS);

        var response = paymentMapper.toCreateResponseDTO(
                paymentService.save(transfer)
        );

        transfer(sourceCw, destinationCw, request.amount());

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                jsonConverter.convertToJson(response),
                OperationType.TRANSFER,
                SUCCESS
        );
    }

    private void transfer(CurrencyWalletEntity sourceCw,
                          CurrencyWalletEntity destinationCw,
                          BigDecimal amount
    ) {

        if (sourceCw.getCurrency().equals(destinationCw.getCurrency())) {
            destinationCw.setBalance(destinationCw.getBalance().add(amount));
            sourceCw.setBalance(sourceCw.getBalance().subtract(amount));
        } else {
            var currencyRate = currencyClientService.getCurrencyRate(
                            sourceCw.getCurrency(),
                            destinationCw.getCurrency())
                    .rate();
            destinationCw.setBalance(destinationCw.getBalance().add(amount.multiply(currencyRate)));
            sourceCw.setBalance(sourceCw.getBalance().subtract(amount));
        }
        currencyWalletService.saveAll(List.of(sourceCw, destinationCw));

    }

}
