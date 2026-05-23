package com.bank.payment.validation.impl;

import com.bank.payment.model.dto.CreateCurrencyWalletRequestDTO;
import com.bank.payment.service.BankAccountService;
import com.bank.payment.validation.OperationValidator;
import com.bank.payment.validation.anno.ValidCurrencyWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import static com.bank.payment.validation.ValidationCode.BANK_ACCOUNT_NOT_FOUND;

@Component
public class CurrencyWalletValidator extends OperationValidator<ValidCurrencyWallet, CreateCurrencyWalletRequestDTO> {

    private final BankAccountService bankAccountService;

    @Autowired
    public CurrencyWalletValidator(BankAccountService bankAccountService, MessageSource messageSource) {
        super(messageSource);
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void validate(CreateCurrencyWalletRequestDTO value) {
        validateBankAccountExists(value.bankAccountId());
    }

    private void validateBankAccountExists(Long bankAccountId) {
        if (bankAccountId != null && bankAccountService.findById(bankAccountId).isEmpty()) {
            addErrorAndMarkNotValid("bankAccountId",
                    BANK_ACCOUNT_NOT_FOUND,
                    "Bank account with id [%d] not found".formatted(bankAccountId),
                    new String[]{bankAccountId.toString()}
            );
        }
    }
}
