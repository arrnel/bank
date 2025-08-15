package com.arrnel.payment.validation.impl;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.data.entity.PaymentEntity;
import com.arrnel.payment.data.entity.RefundEntity;
import com.arrnel.payment.data.enums.PaymentType;
import com.arrnel.payment.model.dto.CreateRefundRequestDTO;
import com.arrnel.payment.service.CurrencyWalletService;
import com.arrnel.payment.service.PaymentService;
import com.arrnel.payment.validation.OperationValidator;
import com.arrnel.payment.validation.ValidationCode;
import com.arrnel.payment.validation.anno.ValidRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.arrnel.payment.data.enums.PaymentType.TRANSFER;
import static com.arrnel.payment.validation.ValidationCode.*;

@Component
public class RefundValidator extends OperationValidator<ValidRefund, CreateRefundRequestDTO> {

    private final CurrencyWalletService currencyWalletService;
    private final PaymentService paymentService;

    @Autowired
    public RefundValidator(CurrencyWalletService currencyWalletService,
                           PaymentService paymentService,
                           MessageSource messageSource
    ) {
        super(messageSource);
        this.currencyWalletService = currencyWalletService;
        this.paymentService = paymentService;
    }


    @Override
    public void validate(CreateRefundRequestDTO request) {

        var paymentEntity = paymentService.findById(request.transferId());
        validatePaymentExists(paymentEntity, request.transferId());
        if (paymentEntity.isEmpty())
            return;

        validatePaymentIsTransfer(paymentEntity.get().getPaymentType());
        if (!paymentEntity.get().getPaymentType().equals(TRANSFER))
            return;

        var currencyWallets = currencyWalletService.findSourceAndDestinationCw(
                paymentEntity.get().getSource().getId(),
                paymentEntity.get().getDestination().getId()
        );
        var sourceCW = Optional.ofNullable(currencyWallets.get(CurrencyWalletService.SOURCE_CURRENCY_WALLET));
        var destinationCW = Optional.ofNullable(currencyWallets.get(CurrencyWalletService.DESTINATION_CURRENCY_WALLET));

        validateSourceCurrencyWalletExists(sourceCW, paymentEntity.get().getSource().getId());
        validateDestinationCurrencyWalletExists(destinationCW, paymentEntity.get().getSource().getId());
        validateMoneyLeftForRefund(paymentEntity.get(), request.amount());

    }

    private void validatePaymentExists(Optional<PaymentEntity> paymentEntity, Long transferId) {
        if (paymentEntity.isEmpty()) {
            addErrorAndMarkNotValid(
                    "transferId",
                    TRANSFER_NOT_FOUND,
                    "Transfer transaction not found by id = [%d]".formatted(transferId),
                    new String[]{transferId.toString()});
        }
    }

    private void validatePaymentIsTransfer(PaymentType paymentType) {
        if (!TRANSFER.equals(paymentType)) {
            addErrorAndMarkNotValid(
                    "transferId",
                    ValidationCode.REFUND_INVALID_PAYMENT_TYPE,
                    "Payment should be transfer for creating new refund",
                    new Object[0]
            );
        }
    }

    private void validateSourceCurrencyWalletExists(Optional<CurrencyWalletEntity> sourceCw, Long sourceCwId) {
        if (sourceCw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "from",
                    REFUND_SOURCE_CURRENCY_WALLET_NOT_FOUND,
                    "Source currency wallet not found by id = [%d]".formatted(sourceCwId),
                    new String[]{sourceCwId.toString()});
        }
    }

    private void validateDestinationCurrencyWalletExists(Optional<CurrencyWalletEntity> sourceCw, Long destinationCwId) {
        if (sourceCw.isEmpty()) {
            addErrorAndMarkNotValid(
                    "to",
                    REFUND_DESTINATION_CURRENCY_WALLET_NOT_FOUND,
                    "Destination currency wallet not found by id = [%d]".formatted(destinationCwId),
                    new String[]{destinationCwId.toString()});
        }
    }

    private void validateMoneyLeftForRefund(PaymentEntity payment, BigDecimal amount) {
        var refundedAmount = payment.getRefunds().stream()
                .map(RefundEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var availableAmount = payment.getAmount().subtract(refundedAmount);
        if (availableAmount.compareTo(amount) < 0) {
            addErrorAndMarkNotValid(
                    "amount",
                    ValidationCode.REFUND_INCREASE_AMOUNT_LIMIT,
                    "Not enough money for refund. Available amount = [%s], expected amount = [%s]".formatted(availableAmount, amount),
                    new String[]{
                            availableAmount.setScale(2, RoundingMode.CEILING).toPlainString(),
                            amount.setScale(2, RoundingMode.CEILING).toPlainString()
                    }
            );
        }
    }

}
