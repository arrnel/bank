package com.arrnel.currency.service.client.currency;

import com.arrnel.currency.data.entity.CurrencyEntity;
import com.arrnel.currency.data.enums.Currency;
import com.arrnel.currency.data.repository.CurrencyRepository;
import com.arrnel.currency.util.JsonConverter;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ParametersAreNonnullByDefault
public abstract class CurrencyClientService {

    private final CurrencyRepository currencyRepository;
    protected final JsonConverter jsonConverter;

    @Nonnull
    protected abstract String getCurrencyData();

    @Nonnull
    protected abstract Map<Currency, BigDecimal> parseCurrencyData(final String currencyData);

    @Transactional
    protected void saveCurrencyData(Map<Currency, BigDecimal> currenciesRateMap) {

        var currencies = currencyRepository.findAll();
        var currenciesRate = currenciesRateMap.entrySet().stream()
                .map(entry -> {
                    var currency = currencies.stream()
                            .filter(ce -> ce.getCurrency().equals(entry.getKey()))
                            .findFirst();
                    // @formatter:off
                    return currency.isPresent()
                            ? currency.get().setRate(entry.getValue())
                            : CurrencyEntity.builder()
                                    .currency(entry.getKey())
                                    .rate(entry.getValue())
                                    .build();
                    // @formatter:on
                })
                .toList();

        currencyRepository.saveAll(currenciesRate);
    }

    public void updateCurrenciesRate() {
        var currencyData = getCurrencyData();
        var currenciesMap = parseCurrencyData(currencyData);
        saveCurrencyData(currenciesMap);
    }

}
