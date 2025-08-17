package com.arrnel.tests.service;


import com.arrnel.tests.config.Config;
import com.arrnel.tests.model.dto.*;
import com.arrnel.tests.util.MapWithWait;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public enum KafkaStore {

    INSTANCE;

    private static final Config CFG = Config.getInstance();

    private static final MapWithWait<String, CreateBankAccountRequestDTO> bankAccountStore = new MapWithWait<>();
    private static final MapWithWait<String, CreateCurrencyWalletRequestDTO> currencyWalletStore = new MapWithWait<>();
    private static final MapWithWait<String, CreateDepositRequestDTO> depositStore = new MapWithWait<>();
    private static final MapWithWait<String, CreateTransferRequestDTO> transferStore = new MapWithWait<>();
    private static final MapWithWait<String, CreateRefundRequestDTO> refundStore = new MapWithWait<>();
    private static final MapWithWait<String, CreateWithdrawalRequestDTO> withdrawalStore = new MapWithWait<>();
    private static final MapWithWait<String, Object> resultStore = new MapWithWait<>();

    public void saveBankAccountRequest(String requestId, CreateBankAccountRequestDTO request) {
        bankAccountStore.put(requestId, request);
    }

    @Nullable
    public CreateBankAccountRequestDTO getBankAccountRequest(String requestId) throws InterruptedException {
        return bankAccountStore.get(requestId, CFG.kafkaMaxTimeoutWaitingResult());
    }

    public void saveCurrencyWalletRequest(String requestId, CreateCurrencyWalletRequestDTO request) {
        currencyWalletStore.put(requestId, request);
    }

    @Nullable
    public CreateCurrencyWalletRequestDTO getCurrencyWalletRequest(String requestId) throws InterruptedException {
        return currencyWalletStore.get(requestId, CFG.kafkaMaxTimeoutWaitingResult());
    }

    public void saveDepositRequest(String requestId, CreateDepositRequestDTO request) {
        depositStore.put(requestId, request);
    }

    @Nullable
    public CreateDepositRequestDTO getDepositRequest(String requestId) throws InterruptedException {
        return depositStore.get(requestId, CFG.kafkaMaxTimeoutWaitingResult());
    }

    public void saveTransferRequest(String requestId, CreateTransferRequestDTO request) {
        transferStore.put(requestId, request);
    }

    @Nullable
    public CreateTransferRequestDTO getTransferRequest(String requestId) throws InterruptedException {
        return transferStore.get(requestId, CFG.kafkaMaxTimeoutWaitingResult());
    }

    public void saveRefundRequest(String requestId, CreateRefundRequestDTO request) {
        refundStore.put(requestId, request);
    }

    @Nullable
    public CreateRefundRequestDTO getRefundRequest(String requestId) throws InterruptedException {
        return refundStore.get(requestId, CFG.kafkaMaxTimeoutWaitingResult());
    }

    public void saveWithdrawalRequest(String requestId, CreateWithdrawalRequestDTO request) {
        withdrawalStore.put(requestId, request);
    }

    @Nullable
    public CreateWithdrawalRequestDTO getWithdrawalRequest(String requestId) throws InterruptedException {
        return withdrawalStore.get(requestId, CFG.kafkaMaxTimeoutWaitingResult());
    }

    public void saveOperationResponse(String requestId, Object request) {
        resultStore.put(requestId, request);
    }

    @Nullable
    public Object getOperationResponse(String requestId) throws InterruptedException {
        var response = resultStore.get(requestId, CFG.kafkaMaxTimeoutWaitingResult());
        resultStore.remove(requestId);
        return response;
    }

}
