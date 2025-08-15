package com.arrnel.payment.validation.impl;

import com.arrnel.payment.model.dto.CreateBankAccountRequestDTO;
import com.arrnel.payment.service.BankAccountService;
import com.arrnel.payment.validation.OperationValidator;
import com.arrnel.payment.validation.ValidationCode;
import com.arrnel.payment.validation.anno.ValidBankAccount;
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
