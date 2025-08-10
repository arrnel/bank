package com.arrnel.gateway.model.dto;

import com.arrnel.gateway.model.enums.PaymentOperationStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
public record CreateOperationResponseDTO(

        @JsonProperty("id")
        Long id,

        @JsonProperty("status")
        PaymentOperationStatus status,

        @JsonProperty("error_message")
        String errorMessage,

        @JsonProperty("created_at")
        LocalDateTime createdAt

) {
}
