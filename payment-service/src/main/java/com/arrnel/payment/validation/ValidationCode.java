package com.arrnel.payment.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationCode {

    // EXCEPTION
    public static final String ILLEGAL_OPERATION_MESSAGE_CODE = "validation.exception.illegal_operation.message";
    public static final String CURRENCY_NOT_FOUND_MESSAGE = "validation.exception.currency_not_found.message";
    public static final String CURRENCY_NOT_FOUND_REASON = "validation.exception.currency_not_found.reason";
    public static final String OPERATION_PROCESSING_MESSAGE = "validation.exception.operation_processing_message";
    public static final String OPERATION_PROCESSING_REASON = "validation.exception.operation_processing_reason";
    public static final String GENERAL_EXCEPTION_MESSAGE = "validation.exception.general_exception_message";

    // BANK_ACCOUNT
    public static final String BANK_ACCOUNT_NOT_FOUND = "validation.bank_account.not_found";
    public static final String BANK_ACCOUNT_USER_ID_ALREADY_HAVE_BANK_ACCOUNT = "validation.bank_account.user.already_exists";

    // DEPOSIT
    public static final String DEPOSIT_CURRENCY_WALLET_NOT_FOUND = "validation.deposit.currency_wallet.not_found";

    // TRANSFER
    public static final String TRANSFER_NOT_FOUND = "validation.transfer.not_found";
    public static final String TRANSFER_CURRENCY_NOT_MATCH = "validation.transfer.currency.not_match";
    public static final String TRANSFER_SOURCE_AND_DESTINATION_CW_HAS_SAME_ID = "validation.transfer.source_and_destination_has_same_id";
    public static final String TRANSFER_SOURCE_CURRENCY_WALLET_NOT_FOUND = "validation.transfer.source.not_found";
    public static final String TRANSFER_DESTINATION_CURRENCY_WALLET_NOT_FOUND = "validation.transfer.destination.not_found";
    public static final String TRANSFER_SENDER_NOT_HAVE_ENOUGH_MONEY = "validation.transfer.source.insufficient_funds";
    public static final String TRANSFER_SENDER_NOT_HAVE_ENOUGH_MONEY_AFTER_CONVERSION = "validation.transfer.source.insufficient_funds_after_conversion";

    // WITHDRAWAL
    public static final String WITHDRAW_CURRENCY_WALLET_NOT_FOUND = "validation.withdraw.currency_wallet.not_found";
    public static final String WITHDRAW_NOT_ENOUGH_MONEY = "validation.withdrawal.insufficient_funds";

    // REFUND
    public static final String REFUND_NOT_FOUND = "validation.refund.not_found";
    public static final String REFUND_INVALID_PAYMENT_TYPE = "validation.refund.payment.invalid_type";
    public static final String REFUND_INCREASE_AMOUNT_LIMIT = "validation.refund.amount.increase_refund_limit";
    public static final String REFUND_SOURCE_CURRENCY_WALLET_NOT_FOUND = "validation.refund.source.not_found";
    public static final String REFUND_DESTINATION_CURRENCY_WALLET_NOT_FOUND = "validation.refund.destination.not_found";

}