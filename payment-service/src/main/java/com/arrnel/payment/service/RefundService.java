package com.arrnel.payment.service;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.data.entity.RefundEntity;
import com.arrnel.payment.data.repository.RefundRepository;
import com.arrnel.payment.mapper.RefundMapper;
import com.arrnel.payment.model.dto.CreateOperationResponseDTO;
import com.arrnel.payment.model.dto.CreateRefundRequestDTO;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.service.client.currency.CurrencyClientService;
import com.arrnel.payment.util.TransactionOperationComponent;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.arrnel.payment.data.enums.OperationStatus.SUCCESS;

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
