package com.arrnel.payment.service.handler.impl;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.mapper.RefundMapper;
import com.arrnel.payment.model.dto.CreateRefundRequestDTO;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.service.PaymentService;
import com.arrnel.payment.service.RefundService;
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
import static com.arrnel.payment.model.enums.OperationType.REFUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefundHandler implements OperationHandler {

    private final CurrencyWalletService currencyWalletService;
    private final CurrencyClientService currencyClientService;
    private final PaymentService paymentService;
    private final RefundService refundService;
    private final RefundMapper refundMapper;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    @Transactional
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateRefundRequestDTO.class);
        validationService.validate(
                requestId,
                REFUND,
                request,
                CreateRefundRequestDTO.class.getSimpleName()
        );

        var payment = paymentService.findById(request.transferId()).get();

        var currencyWallets = currencyWalletService.findSourceAndDestinationCw(
                payment.getSource().getId(),
                payment.getDestination().getId()
        );
        var sourceCW = currencyWallets.get(CurrencyWalletService.SOURCE_CURRENCY_WALLET);
        var destinationCW = currencyWallets.get(CurrencyWalletService.DESTINATION_CURRENCY_WALLET);

        var refund = refundMapper.toEntity(request, payment, SUCCESS);
        var response = refundMapper.toCreateResponseDTO(
                refundService.save(refund)
        );

        refund(sourceCW, destinationCW, request.amount());

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                jsonConverter.convertToJson(response),
                REFUND,
                SUCCESS
        );

    }

    private void refund(CurrencyWalletEntity sourceCw,
                        CurrencyWalletEntity destinationCw,
                        BigDecimal amount
    ) {

        if (sourceCw.getCurrency().equals(destinationCw.getCurrency())) {
            destinationCw.setBalance(destinationCw.getBalance().subtract(amount));
            sourceCw.setBalance(sourceCw.getBalance().add(amount));
        } else {
            var currencyRate = currencyClientService.getCurrencyRate(
                            sourceCw.getCurrency(),
                            destinationCw.getCurrency())
                    .rate();
            destinationCw.setBalance(destinationCw.getBalance().subtract(currencyRate.multiply(amount)));
            sourceCw.setBalance(sourceCw.getBalance().add(amount));
        }
        currencyWalletService.saveAll(List.of(sourceCw, destinationCw));

    }

}
