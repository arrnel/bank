package com.arrnel.tests.model.dto.payment;

import com.arrnel.tests.model.enums.OperationStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
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

        @JsonProperty("operation_number")
        Long operationNumber,

        @JsonProperty("status")
        OperationStatus status,

        @JsonProperty("error_message")
        String errorMessage,

        @JsonProperty("created_at")
        LocalDateTime createdAt

) {

    // NOT REMOVE: jackson can't deserialize properly json
    @JsonCreator
    public CreateOperationResponseDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("operation_number") Long operationNumber,
            @JsonProperty("status") OperationStatus status,
            @JsonProperty("error_message") String errorMessage,
            @JsonProperty("created_at") LocalDateTime createdAt
    ) {
        this.id = id;
        this.operationNumber = operationNumber;
        this.status = status;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }

    public CreateOperationResponseDTO id(Long id) {
        return new CreateOperationResponseDTO(id, this.operationNumber, this.status, this.errorMessage, this.createdAt);
    }

    public CreateOperationResponseDTO operationNumber(Long operationNumber) {
        return new CreateOperationResponseDTO(this.id, operationNumber, this.status, this.errorMessage, this.createdAt);
    }

    public CreateOperationResponseDTO status(OperationStatus status) {
        return new CreateOperationResponseDTO(this.id, this.operationNumber, status, this.errorMessage, this.createdAt);
    }

    public CreateOperationResponseDTO errorMessage(String errorMessage) {
        return new CreateOperationResponseDTO(this.id, this.operationNumber, this.status, errorMessage, this.createdAt);
    }

    public CreateOperationResponseDTO createdAt(LocalDateTime createdAt) {
        return new CreateOperationResponseDTO(this.id, this.operationNumber, this.status, this.errorMessage, createdAt);
    }

}
