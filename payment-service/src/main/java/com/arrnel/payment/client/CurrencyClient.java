package com.arrnel.payment.client;

import com.arrnel.payment.data.enums.Currency;
import com.arrnel.payment.model.dto.currency.CurrencyRateDTO;
import jakarta.annotation.Nonnull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.ParametersAreNonnullByDefault;

@FeignClient(name = "currencyService", url = "${app.integration.internal.currency.url}")
@ParametersAreNonnullByDefault
public interface CurrencyClient {

    @Nonnull
    @GetMapping("/internal/currency/rate")
    CurrencyRateDTO getCurrencyRate(@RequestParam("from") Currency from,
                                    @RequestParam("to") Currency to);

}
