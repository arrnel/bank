package com.arrnel.payment.service.client.currency;

import com.arrnel.payment.client.CurrencyClient;
import com.arrnel.payment.data.enums.Currency;
import com.arrnel.payment.ex.UnableToGetCurrencyException;
import com.arrnel.payment.model.dto.currency.CurrencyRateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class CurrencyClientService {

    private static final String CACHE_NAME = "currency_rates";

    private final CurrencyClient currencyClient;
    private final CacheManager cacheManager;

    @Nonnull
    public CurrencyRateDTO getCurrencyRate(final Currency from, final Currency to) {
        try {
            return currencyClient.getCurrencyRate(from, to);
        } catch (Exception ex) {
            var cacheKey = "%s_%s".formatted(from.name(), to.name());
            var cache = cacheManager.getCache(CACHE_NAME);

            if (cache != null) {
                var cached = cache.get(cacheKey, CurrencyRateDTO.class);
                if (cached != null) {
                    log.warn("Returning cached currency rate for [{} -> {}]", from, to);
                    return cached;
                }
            }

            throw new UnableToGetCurrencyException(from, to, ex);
        }
    }

}
