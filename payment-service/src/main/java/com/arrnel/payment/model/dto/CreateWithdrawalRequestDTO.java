package com.arrnel.payment.model.dto;

import com.arrnel.payment.validation.anno.ValidWithdrawal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@ValidWithdrawal
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateWithdrawalRequestDTO(

        @NotNull(message = "{validation.withdrawal.wallet.not_null}")
        @Positive(message = "{validation.withdrawal.wallet.positive}")
        @JsonProperty("currency_wallet_id")
        Long currencyWalletId,

        @NotNull(message = "{validation.withdrawal.amount.not_null}")
        @Positive(message = "{validation.withdrawal.amount.positive}")
        @JsonProperty("amount")
        BigDecimal amount

) {
}
