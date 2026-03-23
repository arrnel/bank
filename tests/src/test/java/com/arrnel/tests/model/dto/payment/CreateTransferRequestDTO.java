package com.arrnel.tests.model.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateTransferRequestDTO(

        @JsonProperty("operation_number")
        Long operationNumber,

        @JsonProperty("source_id")
        Long sourceId,

        @JsonProperty("destination_id")
        Long destinationId,

        @JsonProperty("amount")
        BigDecimal amount,

        @JsonProperty("comment")
        String comment

) implements Serializable {

    public CreateTransferRequestDTO operationNumber(Long operationNumber) {
        return new CreateTransferRequestDTO(operationNumber, this.sourceId, this.destinationId, this.amount, this.comment);
    }

    public CreateTransferRequestDTO transferId(Long sourceId) {
        return new CreateTransferRequestDTO(this.operationNumber, sourceId, this.destinationId, this.amount, this.comment);
    }

    public CreateTransferRequestDTO destinationId(Long destinationId) {
        return new CreateTransferRequestDTO(this.operationNumber, this.sourceId, destinationId, this.amount, comment);
    }

    public CreateTransferRequestDTO amount(BigDecimal amount) {
        return new CreateTransferRequestDTO(this.operationNumber, this.sourceId, this.destinationId, amount, this.comment);
    }

    public CreateTransferRequestDTO comment(String comment) {
        return new CreateTransferRequestDTO(this.operationNumber, this.sourceId, this.destinationId, this.amount, comment);
    }

}
