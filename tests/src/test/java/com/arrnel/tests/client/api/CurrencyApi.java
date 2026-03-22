package com.arrnel.tests.client.api;

import com.arrnel.tests.model.dto.currency.CurrencyRateDTO;
import com.arrnel.tests.model.enums.Currency;
import feign.Param;
import feign.RequestLine;

import java.math.BigDecimal;

public interface CurrencyApi extends FeignApi {

    @RequestLine("GET /currency/rate?from={from}&to={to}&amount={amount}")
    CurrencyRateDTO getCurrencyRate(
            @Param("from") Currency from,
            @Param("to") Currency to,
            @Param("amount") BigDecimal amount
    );

    @RequestLine("GET /currency/rate?from={from}&to={to}")
    CurrencyRateDTO getCurrencyRate(
            @Param("from") Currency from,
            @Param("to") Currency to
    );

}