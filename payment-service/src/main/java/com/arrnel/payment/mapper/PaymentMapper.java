package com.arrnel.payment.mapper;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.data.entity.PaymentEntity;
import com.arrnel.payment.data.enums.PaymentType;
import com.arrnel.payment.model.dto.CreateDepositRequestDTO;
import com.arrnel.payment.model.dto.CreateOperationResponseDTO;
import com.arrnel.payment.model.dto.CreateTransferRequestDTO;
import com.arrnel.payment.model.dto.CreateWithdrawalRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(CreateDepositRequestDTO requestDTO,
                                  CurrencyWalletEntity currencyWallet
    ) {
        return PaymentEntity.builder()
                .paymentType(PaymentType.DEPOSIT)
                .currency(currencyWallet.getCurrency())
                .source(currencyWallet)
                .amount(requestDTO.amount())
                .build();
    }

    public PaymentEntity toEntity(CreateTransferRequestDTO requestDTO,
                                  CurrencyWalletEntity source,
                                  CurrencyWalletEntity destination
    ) {
        return PaymentEntity.builder()
                .paymentType(PaymentType.TRANSFER)
                .source(source)
                .destination(destination)
                .currency(destination.getCurrency())
                .amount(requestDTO.amount())
                .comment(requestDTO.comment())
                .build();
    }

    public PaymentEntity toEntity(CreateWithdrawalRequestDTO requestDTO,
                                  CurrencyWalletEntity currencyWallet) {
        return PaymentEntity.builder()
                .paymentType(PaymentType.TRANSFER)
                .source(currencyWallet)
                .currency(currencyWallet.getCurrency())
                .amount(requestDTO.amount())
                .build();
    }

    public CreateOperationResponseDTO toCreateResponseDTO(PaymentEntity entity) {
        return CreateOperationResponseDTO.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
