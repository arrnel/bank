package com.arrnel.tests.model.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBankAccountRequestDTO(

        @JsonProperty("operation_number")
        Long operationNumber,

        @JsonProperty("user_id")
        Long userId

) implements Serializable {

    public CreateBankAccountRequestDTO operationNumber(Long operationNumber) {
        return new CreateBankAccountRequestDTO(operationNumber, this.userId);
    }

    public CreateBankAccountRequestDTO userId(Long userId) {
        return new CreateBankAccountRequestDTO(this.operationNumber, userId);
    }

}
