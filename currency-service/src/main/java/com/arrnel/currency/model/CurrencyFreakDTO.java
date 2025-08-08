package com.arrnel.currency.model;

import com.arrnel.currency.data.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Builder
public record CurrencyFreakDTO(

        @JsonProperty("base")
        String baseCurrency,

        @JsonProperty("rates")
        Map<Currency, BigDecimal> rates,

        @JsonProperty("date")
        String dateTime

) implements Serializable {

    public CurrencyFreakDTO {
        if (rates == null)
            rates = new HashMap<>();
    }

}
