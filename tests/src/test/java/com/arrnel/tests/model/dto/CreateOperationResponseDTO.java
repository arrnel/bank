package com.arrnel.tests.model.dto;

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
            @JsonProperty("status") OperationStatus status,
            @JsonProperty("error_message") String errorMessage,
            @JsonProperty("created_at") LocalDateTime createdAt
    ) {
        this.id = id;
        this.status = status;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }

}
