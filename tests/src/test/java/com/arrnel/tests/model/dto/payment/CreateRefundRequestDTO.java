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
public record CreateRefundRequestDTO(

        @JsonProperty("operation_number")
        Long operationNumber,

        @JsonProperty("transfer_id")
        Long transferId,

        @JsonProperty("amount")
        BigDecimal amount,

        @JsonProperty("reason")
        String reason

) implements Serializable {

    public CreateRefundRequestDTO operationNumber(Long operationNumber) {
        return new CreateRefundRequestDTO(operationNumber, this.transferId, this.amount, this.reason);
    }

    public CreateRefundRequestDTO transferId(Long transferId) {
        return new CreateRefundRequestDTO(this.operationNumber, transferId, this.amount, this.reason);
    }

    public CreateRefundRequestDTO amount(BigDecimal amount) {
        return new CreateRefundRequestDTO(this.operationNumber, this.transferId, amount, this.reason);
    }

    public CreateRefundRequestDTO reason(String reason) {
        return new CreateRefundRequestDTO(this.operationNumber, this.transferId, this.amount, reason);
    }

}
