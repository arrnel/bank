package com.arrnel.payment.model.dto;

import com.arrnel.payment.data.enums.Currency;
import com.arrnel.payment.validation.anno.ValidCurrencyWallet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@ValidCurrencyWallet
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateCurrencyWalletRequestDTO(

        @NotNull(message = "{validation.currency_wallet.bank_account_id.not_null}")
        @Positive(message = "{validation.currency_wallet.bank_account_id.positive}")
        @JsonProperty("bank_account_id")
        Long bankAccountId,

        @NotNull(message = "{validation.currency_wallet.currency.not_null}")
        @JsonProperty("currency")
        Currency currency

) implements Serializable {
}
