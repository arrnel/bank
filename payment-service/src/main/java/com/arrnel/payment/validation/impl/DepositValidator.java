package com.arrnel.payment.validation.impl;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.model.dto.CreateDepositRequestDTO;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.service.client.currency.CurrencyClientService;
import com.arrnel.payment.validation.OperationValidator;
import com.arrnel.payment.validation.ValidationCode;
import com.arrnel.payment.validation.anno.ValidDeposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DepositValidator extends OperationValidator<ValidDeposit, CreateDepositRequestDTO> {

    private final CurrencyWalletService currencyWalletService;

    @Autowired
    public DepositValidator(CurrencyWalletService currencyWalletService,
                            CurrencyClientService currencyClientService,
                            MessageSource messageSource
    ) {
        super(messageSource);
        this.currencyWalletService = currencyWalletService;
    }

    @Override
    public void validate(CreateDepositRequestDTO request) {
        var cw = currencyWalletService.findById(request.currencyWalletId());
        validateSourceCurrencyWalletExists(cw, request.currencyWalletId());
    }

    private void validateSourceCurrencyWalletExists(Optional<CurrencyWalletEntity> sourceCw, Long sourceCwId) {
        if (sourceCw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "currencyWalletId",
                    ValidationCode.DEPOSIT_CURRENCY_WALLET_NOT_FOUND,
                    "Currency wallet not found by id = [%d]".formatted(sourceCwId),
                    new String[]{sourceCwId.toString()});
        }
    }

}