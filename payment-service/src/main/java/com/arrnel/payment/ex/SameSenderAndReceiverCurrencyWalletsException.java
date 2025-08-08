package com.arrnel.payment.ex;

public class SameSenderAndReceiverCurrencyWalletsException extends RuntimeException {
    public SameSenderAndReceiverCurrencyWalletsException(String message) {
        super(message);
    }
}
