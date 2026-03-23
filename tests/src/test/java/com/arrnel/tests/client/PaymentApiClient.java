package com.arrnel.tests.client;

import com.arrnel.tests.client.api.GatewayApi;
import com.arrnel.tests.model.dto.payment.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@Slf4j
@ParametersAreNonnullByDefault
public class PaymentApiClient extends RestClient<GatewayApi> {

    public PaymentApiClient(ObjectMapper objectMapper) {
        super(GatewayApi.class, CFG.gatewayApiUrl(), objectMapper);
    }

    @Nonnull
    @Step("Send request POST: /api/v1/payment/bank_account")
    public CreateOperationResponseDTO addNewBankAccount(final CreateBankAccountRequestDTO request) {
        log.info("Send request POST: /api/v1/payment/bank_account");
        return feign.addNewBankAccount(request);
    }

    @Nonnull
    @Step("Send request POST: /payment/currency_wallet")
    public CreateOperationResponseDTO addNewCurrencyWallet(final CreateCurrencyWalletRequestDTO request) {
        log.info("Send request POST: /api/v1/payment/currency_wallet");
        return feign.addNewCurrencyWallet(request);
    }

    @Nonnull
    @Step("Send request POST: /payment/deposit")
    public CreateOperationResponseDTO addNewDeposit(final CreateDepositRequestDTO request) {
        log.info("Send request POST: /api/v1/payment/deposit");
        return feign.addNewDeposit(request);
    }

    @Nonnull
    @Step("Send request POST: /payment/transfer")
    public CreateOperationResponseDTO addNewTransfer(final CreateTransferRequestDTO request) {
        log.info("Send request POST: /api/v1/payment/transfer");
        return feign.addNewTransfer(request);
    }

    @Nonnull
    @Step("Send request POST: /payment/refund")
    public CreateOperationResponseDTO addNewRefund(final CreateRefundRequestDTO request) {
        log.info("Send request POST: /api/v1/payment/refund");
        return feign.addNewRefund(request);
    }

    @Nonnull
    @Step("Send request POST: /payment/withdrawal")
    public CreateOperationResponseDTO addNewWithdrawal(final CreateWithdrawalRequestDTO request) {
        log.info("Send request POST: /api/v1/payment/withdrawal");
        return feign.addNewWithdrawal(request);
    }

}
