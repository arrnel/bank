package com.arrnel.tests.service.rest;

import com.arrnel.tests.client.PaymentApiClient;
import com.arrnel.tests.ex.ApiException;
import com.arrnel.tests.model.dto.ApiErrorDTO;
import com.arrnel.tests.model.dto.payment.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

@Slf4j
@RequiredArgsConstructor
public class PaymentApiService {

    private final PaymentApiClient paymentApiClient;

    public CreateOperationResponseDTO addNewBankAccount(CreateBankAccountRequestDTO request) {
        return paymentApiClient.addNewBankAccount(request);
    }

    @Nonnull
    public ApiErrorDTO addNewBankAccountWithError(CreateBankAccountRequestDTO request) {
        try {
            paymentApiClient.addNewBankAccount(request);
            throw new RuntimeException("Add new bank account response body not contains API error");
        } catch (ApiException ex){
            return ex.getError();
        }
    }

    @Nonnull
    public CreateOperationResponseDTO addNewCurrencyWallet(CreateCurrencyWalletRequestDTO request) {
        log.info("Add new currency wallet: {}", request);
        return paymentApiClient.addNewCurrencyWallet(request);
    }

    @Nonnull
    public ApiErrorDTO addNewCurrencyWalletWithError(CreateCurrencyWalletRequestDTO request) {
        log.info("Try to add new currency wallet: {}", request);
        try {
            paymentApiClient.addNewCurrencyWallet(request);
            throw new RuntimeException("Add new currency wallet response body not contains API error");
        } catch (ApiException ex){
            return ex.getError();
        }
    }

    @Nonnull
    public CreateOperationResponseDTO addNewDeposit(CreateDepositRequestDTO request) {
        log.info("Add new deposit: {}", request);
        return paymentApiClient.addNewDeposit(request);
    }

    @Nonnull
    public ApiErrorDTO addNewDepositWithError(CreateDepositRequestDTO request) {
        log.info("Try to add new deposit: {}", request);
        try {
            paymentApiClient.addNewDeposit(request);
            throw new RuntimeException("Add new deposit response body not contains API error");
        } catch (ApiException ex){
            return ex.getError();
        }
    }

    @Nonnull
    public CreateOperationResponseDTO addNewTransfer(CreateTransferRequestDTO request) {
        log.info("Add new deposit: {}", request);
        return paymentApiClient.addNewTransfer(request);
    }

    @Nonnull
    public ApiErrorDTO addNewTransferWithError(CreateTransferRequestDTO request) {
        log.info("Try to add new transfer: {}", request);
        try {
            paymentApiClient.addNewTransfer(request);
            throw new RuntimeException("Add new transfer response body not contains API error");
        } catch (ApiException ex){
            return ex.getError();
        }
    }

    @Nonnull
    public CreateOperationResponseDTO addNewRefund(CreateRefundRequestDTO request) {
        log.info("Add new refund: {}", request);
        return paymentApiClient.addNewRefund(request);
    }

    @Nonnull
    public ApiErrorDTO addNewRefundWithError(CreateRefundRequestDTO request) {
        log.info("Try to add new refund: {}", request);
        try {
            paymentApiClient.addNewRefund(request);
            throw new RuntimeException("Add new refund response body not contains API error");
        } catch (ApiException ex){
            return ex.getError();
        }
    }

    @Nonnull
    public CreateOperationResponseDTO addNewRefund(CreateWithdrawalRequestDTO request) {
        log.info("Add new withdrawal: {}", request);
        return paymentApiClient.addNewWithdrawal(request);
    }

    @Nonnull
    public ApiErrorDTO addNewRefundWithError(CreateWithdrawalRequestDTO request) {
        log.info("Try to add new withdrawal: {}", request);
        try {
            paymentApiClient.addNewWithdrawal(request);
            throw new RuntimeException("Add new withdrawal response body not contains API error");
        } catch (ApiException ex){
            return ex.getError();
        }
    }

}
