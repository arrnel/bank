package com.arrnel.payment.mapper;

import com.arrnel.payment.data.entity.BankAccountEntity;
import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.data.enums.OperationStatus;
import com.arrnel.payment.model.dto.CreateCurrencyWalletRequestDTO;
import com.arrnel.payment.model.dto.CreateOperationResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyWalletMapper {

    public CurrencyWalletEntity toEntity(CreateCurrencyWalletRequestDTO requestDTO, BankAccountEntity bankAccountEntity) {
        return CurrencyWalletEntity.builder()
                .bankAccount(bankAccountEntity)
                .currency(requestDTO.currency())
                .balance(BigDecimal.ZERO)
                .build();
    }

    public CreateOperationResponseDTO toCreateResponseDTO(CurrencyWalletEntity entity, OperationStatus status) {
        return CreateOperationResponseDTO.builder()
                .id(entity.getId())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .status(status)
                .build();
    }

}
