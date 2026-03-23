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
public record CreateWithdrawalRequestDTO(

        @JsonProperty("operation_number")
        Long operationNumber,

        @JsonProperty("currency_wallet_id")
        Long currencyWalletId,

        @JsonProperty("amount")
        BigDecimal amount

) {

    public CreateWithdrawalRequestDTO operationNumber(Long operationNumber) {
        return new CreateWithdrawalRequestDTO(operationNumber, this.currencyWalletId, this.amount);
    }

    public CreateWithdrawalRequestDTO currencyWalletId(Long currencyWalletId) {
        return new CreateWithdrawalRequestDTO(this.operationNumber, currencyWalletId, this.amount);
    }

    public CreateWithdrawalRequestDTO amount(BigDecimal amount) {
        return new CreateWithdrawalRequestDTO(this.operationNumber, this.currencyWalletId, amount);
    }

}
