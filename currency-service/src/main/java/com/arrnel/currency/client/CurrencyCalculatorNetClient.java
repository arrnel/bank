package com.arrnel.currency.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.ParametersAreNonnullByDefault;

@FeignClient(name = "currencyCalculatorNet", url = "${app.integration.external.currency.calculator_net.url}")
@ParametersAreNonnullByDefault
public interface CurrencyCalculatorNetClient {

    @GetMapping
    String getHtmlWithCurrencies(@RequestParam("eamount") String amount,
                                 @RequestParam("efrom") String from,
                                 @RequestParam("eto") String to);

}
