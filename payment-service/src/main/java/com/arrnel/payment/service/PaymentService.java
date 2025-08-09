package com.arrnel.payment.service;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.data.entity.PaymentEntity;
import com.arrnel.payment.data.repository.PaymentRepository;
import com.arrnel.payment.mapper.PaymentMapper;
import com.arrnel.payment.model.dto.CreateDepositRequestDTO;
import com.arrnel.payment.model.dto.CreateOperationResponseDTO;
import com.arrnel.payment.model.dto.CreateTransferRequestDTO;
import com.arrnel.payment.model.dto.CreateWithdrawalRequestDTO;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.service.client.currency.CurrencyClientService;
import com.arrnel.payment.util.TransactionOperationComponent;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

import static com.arrnel.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class PaymentService {

    private final CurrencyClientService currencyClientService;
    private final CurrencyWalletService currencyWalletService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final TransactionOperationComponent txComponent;

    @Nonnull
    @Transactional
    public PaymentEntity save(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Optional<PaymentEntity> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public CreateOperationResponseDTO addDeposit(CreateDepositRequestDTO request) {
        var currencyWallet = currencyWalletService.findById(request.currencyWalletId()).get();
        var deposit = paymentMapper.toEntity(request, currencyWallet)
                .setStatus(SUCCESS);
        log.info("Add deposit: {}", deposit);
        return paymentMapper.toCreateResponseDTO(
                doDeposit(currencyWallet, deposit));
    }

    @Nonnull
    public CreateOperationResponseDTO transfer(CreateTransferRequestDTO request) {
        var currencyWallets = currencyWalletService.findSourceAndDestinationCw(request.sourceId(), request.destinationId());
        var sourceCw = currencyWallets.get(CurrencyWalletService.SOURCE_CURRENCY_WALLET);
        var destinationCw = currencyWallets.get(CurrencyWalletService.DESTINATION_CURRENCY_WALLET);

        var transfer = paymentMapper.toEntity(request, sourceCw, destinationCw)
                .setStatus(SUCCESS);

        log.info("Transfer: {}", transfer);
        return paymentMapper.toCreateResponseDTO(
                doTransfer(sourceCw, destinationCw, transfer));
    }

    @Nonnull
    public CreateOperationResponseDTO withdrawal(CreateWithdrawalRequestDTO request) {
        var currencyWallet = currencyWalletService.findById(request.currencyWalletId()).get();
        var withdrawal = paymentMapper.toEntity(request, currencyWallet)
                .setStatus(SUCCESS);
        log.info("Withdraw: {}", withdrawal);
        return paymentMapper.toCreateResponseDTO(
                doWithdraw(currencyWallet, withdrawal));
    }

    @Nonnull
    private PaymentEntity doDeposit(CurrencyWalletEntity currencyWallet, PaymentEntity deposit) {
        return txComponent.doInTransaction(OperationType.TRANSFER, () -> {
            currencyWallet.setBalance(currencyWallet.getBalance().add(deposit.getAmount()));
            currencyWalletService.save(currencyWallet);
            return save(deposit);
        });
    }

    @Nonnull
    private PaymentEntity doTransfer(CurrencyWalletEntity sourceCw, CurrencyWalletEntity destinationCw, PaymentEntity transfer) {
        return txComponent.doInTransaction(OperationType.TRANSFER, () -> {
            var convertedAmount = transfer.getAmount();
            if (!sourceCw.getCurrency().equals(destinationCw.getCurrency())) {
                var currencyRate = currencyClientService.getCurrencyRate(
                                sourceCw.getCurrency(),
                                destinationCw.getCurrency())
                        .rate();
                convertedAmount = transfer.getAmount().multiply(currencyRate);
            }
            destinationCw.setBalance(destinationCw.getBalance().add(convertedAmount));
            sourceCw.setBalance(sourceCw.getBalance().subtract(transfer.getAmount()));
            currencyWalletService.saveAll(List.of(sourceCw, destinationCw));
            return save(transfer);
        });
    }

    @Nonnull
    private PaymentEntity doWithdraw(CurrencyWalletEntity currencyWallet, PaymentEntity withdrawal) {
        return txComponent.doInTransaction(OperationType.WITHDRAWAL, () -> {
            currencyWallet.setBalance(currencyWallet.getBalance().subtract(withdrawal.getAmount()));
            currencyWalletService.save(currencyWallet);
            return save(withdrawal);
        });
    }

}
