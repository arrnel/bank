package com.arrnel.tests.service.rest;

import com.arrnel.tests.client.CurrencyApiClient;
import com.arrnel.tests.ex.ApiException;
import com.arrnel.tests.model.dto.ApiErrorDTO;
import com.arrnel.tests.model.dto.currency.CurrencyRateDTO;
import com.arrnel.tests.model.enums.Currency;
import io.qameta.allure.Allure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class CurrencyApiService {

    private final CurrencyApiClient currencyApiClient;

    @Nonnull
    public CurrencyRateDTO getCurrencyRate(final Currency from, final Currency to) {
        return getCurrencyRate(from, to, null);
    }

    @Nonnull
    public CurrencyRateDTO getCurrencyRate(final Currency from, final Currency to, @Nullable BigDecimal amount) {
        var step = amount == null
                ? "Get currency exchange rate: [{%s}] -> [{%s}]".formatted(from, to)
                : "Get currency exchange rate: [{%s}] -> [{%s}]. Amount = [{%s}]".formatted(from, to, amount);
        log.info(step);
        return Allure.step(step, () -> currencyApiClient.getCurrencyRate(from, to, amount));
    }

    @Nonnull
    public ApiErrorDTO getCurrencyRateWithError(final Currency from, final Currency to) {
        return getCurrencyRateWithError(from, to, null);
    }

    @Nonnull
    public ApiErrorDTO getCurrencyRateWithError(final Currency from, final Currency to, @Nullable BigDecimal amount) {
        try {
            getCurrencyRate(from, to, amount);
            throw new RuntimeException("Get currency rate response body not contains API error");
        } catch (ApiException ex) {
            return ex.getError();
        }
    }

}
