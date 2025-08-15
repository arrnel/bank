package com.arrnel.tests.model.dto;

import com.arrnel.tests.model.enums.Currency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateCurrencyWalletRequestDTO(

        @JsonProperty("bank_account_id")
        Long bankAccountId,

        @JsonProperty("currency")
        Currency currency

) implements Serializable {
}
