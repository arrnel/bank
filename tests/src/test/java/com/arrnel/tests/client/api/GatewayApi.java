package com.arrnel.tests.client.api;

import com.arrnel.tests.model.dto.payment.*;
import feign.RequestLine;

public interface GatewayApi extends FeignApi {

    @RequestLine("POST /payment/bank_account")
    CreateOperationResponseDTO addNewBankAccount(CreateBankAccountRequestDTO requestBody);

    @RequestLine("POST /payment/currency_wallet")
    CreateOperationResponseDTO addNewCurrencyWallet(CreateCurrencyWalletRequestDTO requestBody);

    @RequestLine("POST /payment/deposit")
    CreateOperationResponseDTO addNewDeposit(CreateDepositRequestDTO requestBody);

    @RequestLine("POST /payment/transfer")
    CreateOperationResponseDTO addNewTransfer(CreateTransferRequestDTO requestBody);

    @RequestLine("POST /payment/refund")
    CreateOperationResponseDTO addNewRefund(CreateRefundRequestDTO requestBody);

    @RequestLine("POST /payment/withdrawal")
    CreateOperationResponseDTO addNewWithdrawal(CreateWithdrawalRequestDTO requestBody);

}
