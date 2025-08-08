package com.arrnel.payment.validation.impl;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.model.dto.CreateWithdrawalRequestDTO;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.validation.OperationValidator;
import com.arrnel.payment.validation.ValidationCode;
import com.arrnel.payment.validation.anno.ValidWithdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class WithdrawalValidator extends OperationValidator<ValidWithdrawal, CreateWithdrawalRequestDTO> {

    private final CurrencyWalletService currencyWalletService;

    @Autowired
    public WithdrawalValidator(MessageSource messageSource,
                               CurrencyWalletService currencyWalletService
    ) {
        super(messageSource);
        this.currencyWalletService = currencyWalletService;
    }

    @Override
    public void validate(CreateWithdrawalRequestDTO request) {
        var cw = currencyWalletService.findById(request.currencyWalletId());
        validateCurrencyWalletExists(cw, request.currencyWalletId());
        if (cw.isEmpty())
            return;
        validateMoneyForWithdrawal(cw.get().getBalance(), request.amount());
    }

    private void validateCurrencyWalletExists(Optional<CurrencyWalletEntity> cw, Long cwId) {
        if (cw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "from",
                    ValidationCode.WITHDRAW_CURRENCY_WALLET_NOT_FOUND,
                    "Currency wallet not found by id = [%d]".formatted(cwId),
                    new Long[]{cwId});
        }
    }

    private void validateMoneyForWithdrawal(BigDecimal actualAmount,
                                            BigDecimal amount
    ) {

        if (actualAmount.compareTo(amount) < 0) {
            addErrorAndMarkNotValid(
                    "amount",
                    ValidationCode.WITHDRAW_NOT_ENOUGH_MONEY,
                    "Not enough money for withdraw. Available currency wallet amount = [%s], expected amount = [%s]".formatted(actualAmount, amount),
                    new BigDecimal[]{actualAmount, amount}
            );
        }
    }

}
