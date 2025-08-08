package com.arrnel.currency.model;

import com.arrnel.currency.data.enums.Currency;
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
public record CurrencyRateResponseDTO(

        @JsonProperty("from")
        Currency from,

        @JsonProperty("to")
        Currency to,

        @JsonProperty("rate")
        BigDecimal rate

) implements Serializable {
}
