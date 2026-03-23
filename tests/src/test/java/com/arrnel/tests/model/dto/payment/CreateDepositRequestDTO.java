package com.arrnel.tests.model.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateDepositRequestDTO(

        @JsonProperty("operation_number")
        Long operationNumber,

        @JsonProperty("currency_wallet_id")
        Long currencyWalletId,

        @JsonProperty("amount")
        BigDecimal amount

) {

    public CreateDepositRequestDTO operationNumber(Long operationNumber) {
        return new CreateDepositRequestDTO(operationNumber, this.currencyWalletId, this.amount);
    }

    public CreateDepositRequestDTO currencyWalletId(Long currencyWalletId) {
        return new CreateDepositRequestDTO(this.operationNumber, currencyWalletId, this.amount);
    }

    public CreateDepositRequestDTO amount(BigDecimal amount) {
        return new CreateDepositRequestDTO(this.operationNumber, this.currencyWalletId, amount);
    }

}
