package com.arrnel.payment.mapper;

import com.arrnel.payment.data.entity.BankAccountEntity;
import com.arrnel.payment.data.enums.OperationStatus;
import com.arrnel.payment.model.dto.CreateBankAccountRequestDTO;
import com.arrnel.payment.model.dto.CreateOperationResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccountEntity toEntity(CreateBankAccountRequestDTO requestDTO) {
        return BankAccountEntity.builder()
                .userId(requestDTO.userId())
                .build();
    }

    public CreateOperationResponseDTO toCreateResponseDTO(BankAccountEntity bankAccountEntity, OperationStatus status) {
        return CreateOperationResponseDTO.builder()
                .id(bankAccountEntity.getId())
                .errorMessage(bankAccountEntity.getErrorMessage())
                .createdAt(bankAccountEntity.getCreatedAt())
                .status(status)
                .build();
    }

}
