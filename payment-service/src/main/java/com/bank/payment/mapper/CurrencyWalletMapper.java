package com.bank.payment.mapper;

import com.bank.payment.data.entity.BankAccountEntity;
import com.bank.payment.data.entity.CurrencyWalletEntity;
import com.bank.payment.data.enums.OperationStatus;
import com.bank.payment.model.dto.CreateCurrencyWalletRequestDTO;
import com.bank.payment.model.dto.CreateOperationResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyWalletMapper {

    public CurrencyWalletEntity toEntity(CreateCurrencyWalletRequestDTO requestDTO, BankAccountEntity bankAccountEntity) {
        return CurrencyWalletEntity.builder()
                .operationNumber(requestDTO.operationNumber())
                .bankAccount(bankAccountEntity)
                .currency(requestDTO.currency())
                .balance(BigDecimal.ZERO)
                .build();
    }

    public CreateOperationResponseDTO toCreateResponseDTO(CurrencyWalletEntity entity, OperationStatus status) {
        return CreateOperationResponseDTO.builder()
                .id(entity.getId())
                .operationNumber(entity.getOperationNumber())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .status(status)
                .build();
    }

}
