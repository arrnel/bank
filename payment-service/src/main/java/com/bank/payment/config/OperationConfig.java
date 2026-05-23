package com.bank.payment.config;

import com.bank.payment.model.enums.OperationType;
import com.bank.payment.service.handler.OperationHandler;
import com.bank.payment.service.handler.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OperationConfig {

    @Bean
    public Map<OperationType, OperationHandler> operationHandlers(
            CreateBankAccountHandler createBankAccountHandler,
            CreateCurrencyWalletHandler createCurrencyWalletHandler,
            DepositHandler depositHandler,
            TransferHandler transferHandler,
            WithdrawalHandler withdrawalHandler,
            RefundHandler refundHandler
    ) {
        Map<OperationType, OperationHandler> operationHandlersMap = new HashMap<>();
        operationHandlersMap.put(OperationType.CREATE_BANK_ACCOUNT, createBankAccountHandler);
        operationHandlersMap.put(OperationType.CREATE_CURRENCY_WALLET, createCurrencyWalletHandler);
        operationHandlersMap.put(OperationType.DEPOSIT, depositHandler);
        operationHandlersMap.put(OperationType.TRANSFER, transferHandler);
        operationHandlersMap.put(OperationType.WITHDRAWAL, withdrawalHandler);
        operationHandlersMap.put(OperationType.REFUND, refundHandler);
        return operationHandlersMap;
    }

}
