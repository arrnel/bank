package com.arrnel.payment.validation.impl;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.model.dto.CreateTransferRequestDTO;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.validation.OperationValidator;
import com.arrnel.payment.validation.ValidationCode;
import com.arrnel.payment.validation.anno.ValidTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.arrnel.payment.validation.ValidationCode.TRANSFER_SENDER_NOT_HAVE_ENOUGH_MONEY;
import static com.arrnel.payment.validation.ValidationCode.TRANSFER_SOURCE_AND_DESTINATION_CW_HAS_SAME_ID;

@Component
public class TransferValidator extends OperationValidator<ValidTransfer, CreateTransferRequestDTO> {

    private final CurrencyWalletService currencyWalletService;

    @Autowired
    public TransferValidator(CurrencyWalletService currencyWalletService,
                             MessageSource messageSource
    ) {
        super(messageSource);
        this.currencyWalletService = currencyWalletService;
    }

    @Override
    public void validate(CreateTransferRequestDTO request) {

        validateSourceAndDestinationWalletsAreNotTheSame(request.sourceId(), request.destinationId());
        if (request.sourceId().equals(request.destinationId()))
            return;

        var currencyWallets = currencyWalletService.findSourceAndDestinationCw(request.sourceId(), request.destinationId());
        var sourceCW = Optional.ofNullable(currencyWallets.get(CurrencyWalletService.SOURCE_CURRENCY_WALLET));
        var destinationCW = Optional.ofNullable(currencyWallets.get(CurrencyWalletService.DESTINATION_CURRENCY_WALLET));

        validateSourceCurrencyWalletExists(sourceCW, request.sourceId());
        validateDestinationCurrencyWalletExists(destinationCW, request.destinationId());

        if (sourceCW.isPresent() && destinationCW.isPresent())
            validateMoneyForTransfer(sourceCW.get().getBalance(), request.amount());
    }

    private void validateSourceAndDestinationWalletsAreNotTheSame(Long sourceCwId, Long destinationCwId) {
        if (sourceCwId.equals(destinationCwId)) {
            addErrorAndMarkNotValid(
                    "destinationId",
                    TRANSFER_SOURCE_AND_DESTINATION_CW_HAS_SAME_ID,
                    "Source and destination currency wallet ids should not be the same",
                    new String[]{sourceCwId.toString(), destinationCwId.toString()}
            );
        }
    }

    private void validateSourceCurrencyWalletExists(Optional<CurrencyWalletEntity> sourceCw, Long sourceCwId) {
        if (sourceCw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "from",
                    ValidationCode.TRANSFER_SOURCE_CURRENCY_WALLET_NOT_FOUND,
                    "Source currency wallet not found by id = [%d]".formatted(sourceCwId),
                    new String[]{sourceCwId.toString()});
        }
    }

    private void validateDestinationCurrencyWalletExists(Optional<CurrencyWalletEntity> sourceCw, Long destinationCwId) {
        if (sourceCw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "to",
                    ValidationCode.TRANSFER_DESTINATION_CURRENCY_WALLET_NOT_FOUND,
                    "Destination currency wallet not found by id = [%d]".formatted(destinationCwId),
                    new String[]{destinationCwId.toString()});
        }
    }

    private void validateMoneyForTransfer(BigDecimal sourceBalance,
                                          BigDecimal amount
    ) {
        if (sourceBalance.compareTo(amount) < 0) {
            addErrorAndMarkNotValid(
                    "amount",
                    TRANSFER_SENDER_NOT_HAVE_ENOUGH_MONEY,
                    "Not enough money for transfer. Available currency wallet amount = [%s], expected amount = [%s]".formatted(sourceBalance, amount),
                    new String[]{
                            sourceBalance.setScale(2, RoundingMode.CEILING).toPlainString(), // Flaky can return [0.0000] instead of [0.00]
                            amount.setScale(2, RoundingMode.CEILING).toPlainString()
                    }
            );
        }
    }

}