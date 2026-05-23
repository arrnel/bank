package com.bank.payment.service;

import com.bank.payment.data.entity.CurrencyWalletEntity;
import com.bank.payment.data.entity.RefundEntity;
import com.bank.payment.data.repository.RefundRepository;
import com.bank.payment.mapper.RefundMapper;
import com.bank.payment.model.dto.CreateOperationResponseDTO;
import com.bank.payment.model.dto.CreateRefundRequestDTO;
import com.bank.payment.model.enums.OperationType;
import com.bank.payment.service.client.currency.CurrencyClientService;
import com.bank.payment.util.TransactionOperationComponent;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.bank.payment.data.enums.OperationStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final CurrencyClientService currencyClientService;
    private final CurrencyWalletService currencyWalletService;
    private final PaymentService paymentService;
    private final RefundMapper refundMapper;
    private final TransactionOperationComponent txComponent;

    @Nonnull
    @Transactional
    public RefundEntity save(RefundEntity entity) {
        return refundRepository.save(entity);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Optional<RefundEntity> getById(Long id) {
        return refundRepository.findById(id);
    }

    @Nonnull
    public CreateOperationResponseDTO addRefund(CreateRefundRequestDTO request) {
        var payment = paymentService.findById(request.transferId()).get();

        var currencyWallets = currencyWalletService.findSourceAndDestinationCw(
                payment.getSource().getId(),
                payment.getDestination().getId()
        );
        var sourceCw = currencyWallets.get(CurrencyWalletService.SOURCE_CURRENCY_WALLET);
        var destinationCW = currencyWallets.get(CurrencyWalletService.DESTINATION_CURRENCY_WALLET);

        var refund = refundMapper.toEntity(request, payment, SUCCESS);

        return refundMapper.toCreateResponseDTO(
                doRefund(sourceCw, destinationCW, refund)
        );
    }

    private RefundEntity doRefund(CurrencyWalletEntity sourceCw,
                                  CurrencyWalletEntity destinationCw,
                                  RefundEntity refund
    ) {
        return txComponent.doInTransaction(OperationType.REFUND, () -> {
            var convertedAmount = refund.getAmount();
            if (!sourceCw.getCurrency().equals(destinationCw.getCurrency())) {
                var currencyRate = currencyClientService.getCurrencyRate(
                                sourceCw.getCurrency(),
                                destinationCw.getCurrency())
                        .rate();
                convertedAmount = currencyRate.multiply(convertedAmount);
            }

            destinationCw.setBalance(destinationCw.getBalance().subtract(convertedAmount));
            sourceCw.setBalance(sourceCw.getBalance().add(refund.getAmount()));
            currencyWalletService.saveAll(List.of(sourceCw, destinationCw));
            return save(refund);
        });
    }

}
