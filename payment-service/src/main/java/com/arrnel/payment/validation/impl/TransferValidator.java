package com.arrnel.payment.validation.impl;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.data.enums.Currency;
import com.arrnel.payment.model.dto.CreateTransferRequestDTO;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.service.client.currency.CurrencyClientService;
import com.arrnel.payment.validation.OperationValidator;
import com.arrnel.payment.validation.ValidationCode;
import com.arrnel.payment.validation.anno.ValidTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

import static com.arrnel.payment.validation.ValidationCode.*;

@Component
public class TransferValidator extends OperationValidator<ValidTransfer, CreateTransferRequestDTO> {

    private final CurrencyWalletService currencyWalletService;
    private final CurrencyClientService currencyClientService;

    @Autowired
    public TransferValidator(CurrencyWalletService currencyWalletService,
                             CurrencyClientService currencyClientService,
                             MessageSource messageSource
    ) {
        super(messageSource);
        this.currencyWalletService = currencyWalletService;
        this.currencyClientService = currencyClientService;
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

        if (sourceCW.isPresent() && destinationCW.isPresent()) {
            validateMoneyForTransfer(
                    sourceCW.get().getBalance(),
                    sourceCW.get().getCurrency(),
                    destinationCW.get().getCurrency(),
                    request.amount()
            );
        }
    }

    private void validateSourceAndDestinationWalletsAreNotTheSame(Long sourceCwId, Long destinationCwId) {
        if (sourceCwId.equals(destinationCwId)) {
            addErrorAndMarkNotValid(
                    "destinationId",
                    TRANSFER_SOURCE_AND_DESTINATION_CW_HAS_SAME_ID,
                    "Source and destination currency wallet ids should not be the same",
                    new Long[]{sourceCwId, destinationCwId}
            );
        }
    }

    private void validateSourceCurrencyWalletExists(Optional<CurrencyWalletEntity> sourceCw, Long sourceCwId) {
        if (sourceCw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "from",
                    ValidationCode.TRANSFER_SOURCE_CURRENCY_WALLET_NOT_FOUND,
                    "Source currency wallet not found by id = [%d]".formatted(sourceCwId),
                    new Long[]{sourceCwId});
        }
    }

    private void validateDestinationCurrencyWalletExists(Optional<CurrencyWalletEntity> sourceCw, Long destinationCwId) {
        if (sourceCw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "to",
                    ValidationCode.TRANSFER_DESTINATION_CURRENCY_WALLET_NOT_FOUND,
                    "Destination currency wallet not found by id = [%d]".formatted(destinationCwId),
                    new Long[]{destinationCwId});
        }
    }

    private void validateMoneyForTransfer(BigDecimal sourceBalance,
                                          Currency sourceCurrency,
                                          Currency destinationCurrency,
                                          BigDecimal amount
    ) {

        if (sourceCurrency.equals(destinationCurrency)) {
            if (sourceBalance.compareTo(amount) < 0) {
                addErrorAndMarkNotValid(
                        "amount",
                        TRANSFER_SENDER_NOT_HAVE_ENOUGH_MONEY,
                        "Not enough money for transfer. Available currency wallet amount = [%s], expected amount = [%s]".formatted(sourceBalance, amount),
                        new BigDecimal[]{sourceBalance, amount}
                );
            }
        } else {
            var currencyRate = currencyClientService.getCurrencyRate(
                            sourceCurrency,
                            destinationCurrency)
                    .rate();
            var requiredAmount = amount.multiply(currencyRate);
            if (sourceBalance.compareTo(requiredAmount) < 0) {
                addErrorAndMarkNotValid(
                        "amount",
                        TRANSFER_SENDER_NOT_HAVE_ENOUGH_MONEY_AFTER_CONVERSION,
                        "Not enough money for transfer. Converted available currency wallet amount = [%s], expected amount = [%s]".formatted(sourceBalance, amount),
                        new BigDecimal[]{sourceBalance, amount}
                );
            }
        }
    }

}