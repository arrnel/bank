package com.arrnel.payment.mapper;

import com.arrnel.payment.data.entity.PaymentEntity;
import com.arrnel.payment.data.entity.RefundEntity;
import com.arrnel.payment.data.enums.OperationStatus;
import com.arrnel.payment.model.dto.CreateOperationResponseDTO;
import com.arrnel.payment.model.dto.CreateRefundRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class RefundMapper {

    public RefundEntity toEntity(CreateRefundRequestDTO requestDTO,
                                 PaymentEntity payment,
                                 OperationStatus status
    ) {
        return RefundEntity.builder()
                .operationNumber(requestDTO.operationNumber())
                .payment(payment)
                .currency(payment.getCurrency())
                .amount(requestDTO.amount())
                .status(status)
                .build();
    }


    public CreateOperationResponseDTO toCreateResponseDTO(RefundEntity entity) {
        return CreateOperationResponseDTO.builder()
                .id(entity.getId())
                .operationNumber(entity.getOperationNumber())
                .status(entity.getStatus())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
