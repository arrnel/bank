package com.arrnel.tests.model.dto;

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

        @JsonProperty("currency_wallet_id")
        Long currencyWalletId,

        @JsonProperty("amount")
        BigDecimal amount

) {
}
