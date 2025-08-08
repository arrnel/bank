package com.arrnel.currency.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.ParametersAreNonnullByDefault;

@FeignClient(name = "currencyFreaks", url = "${app.integration.external.currency.currency_freak.url}")
@ParametersAreNonnullByDefault
public interface CurrencyFreaksClient {

    @GetMapping("/latest")
    String getLatestCurrenciesRates(@RequestParam("apikey") String apiKey,
                                    @RequestParam("base") String baseCurrency,
                                    @RequestParam(value = "symbols", required = false) String symbols
    );

}
