package com.bank.payment.validation.impl;

import com.bank.payment.model.dto.CreateBankAccountRequestDTO;
import com.bank.payment.service.BankAccountService;
import com.bank.payment.validation.OperationValidator;
import com.bank.payment.validation.ValidationCode;
import com.bank.payment.validation.anno.ValidBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class BankAccountValidator extends OperationValidator<ValidBankAccount, CreateBankAccountRequestDTO> {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountValidator(BankAccountService bankAccountService, MessageSource messageSource) {
        super(messageSource);
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void validate(CreateBankAccountRequestDTO request) {
        validateBankAccountNotExists(request.userId());
    }

    private void validateBankAccountNotExists(Long userId) {
        if (bankAccountService.existsByUserId(userId))
            addErrorAndMarkNotValid(
                    "userId",
                    ValidationCode.BANK_ACCOUNT_USER_ID_ALREADY_HAVE_BANK_ACCOUNT,
                    "User with id = [%d] already have bank account".formatted(userId),
                    new String[]{userId.toString()}
            );
    }
}
