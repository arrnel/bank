package com.arrnel.tests.client;

import com.arrnel.tests.client.api.CurrencyApi;
import com.arrnel.tests.model.dto.currency.CurrencyRateDTO;
import com.arrnel.tests.model.enums.Currency;
import com.arrnel.tests.util.JsonConverter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;

@Slf4j
@ParametersAreNonnullByDefault
public class CurrencyApiClient extends RestClient<CurrencyApi> {

    public CurrencyApiClient(JsonConverter jsonConverter) {
        super(CurrencyApi.class, CFG.gatewayApiUrl(), jsonConverter.getObjectMapper());
    }

    @Nonnull
    public CurrencyRateDTO getCurrencyRate(final Currency from, final Currency to, @Nullable BigDecimal amount) {
        var amountSubQuery = amount == null
                ? ""
                : "&amount=%s".formatted(amount.toPlainString());
        log.info("Send request GET: {}/currency/rate?from={}&to={}{}", CFG.gatewayApiUrl(), from, to, amountSubQuery);
        return feign.getCurrencyRate(from, to);
    }

}
