package com.arrnel.gateway.client.rest;

import com.arrnel.gateway.model.dto.CurrencyRateDTO;
import com.arrnel.gateway.model.enums.Currency;
import jakarta.annotation.Nullable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;

@FeignClient(name = "currencyService", url = "${app.integration.internal.currency.url}")
@ParametersAreNonnullByDefault
public interface CurrencyClient {

    @Nonnull
    @GetMapping("/internal/currency/rate")
    ResponseEntity<CurrencyRateDTO> getCurrencyRate(@RequestParam("from") Currency from,
                                                    @RequestParam("to") Currency to,
                                                    @Nullable @RequestParam("amount") BigDecimal amount);

}

