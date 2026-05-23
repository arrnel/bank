package com.bank.currency.service.client.currency.impl;

import com.bank.currency.client.CurrencyFreaksClient;
import com.bank.currency.data.enums.Currency;
import com.bank.currency.data.repository.CurrencyRepository;
import com.bank.currency.ex.UnableToUpdateCurrenciesException;
import com.bank.currency.model.CurrencyFreakDTO;
import com.bank.currency.service.client.currency.CurrencyClientService;
import com.bank.currency.util.JsonConverter;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@ParametersAreNonnullByDefault
public class CurrencyFreaksServiceClient extends CurrencyClientService {

    private final CurrencyFreaksClient currencyFreaksClient;

    @Autowired
    public CurrencyFreaksServiceClient(CurrencyRepository currencyRepository,
                                       JsonConverter jsonConverter,
                                       CurrencyFreaksClient currencyFreaksClient
    ) {
        super(currencyRepository, jsonConverter);
        this.currencyFreaksClient = currencyFreaksClient;
    }

    @Value("${app.integration.external.currency.currency_freak.api_key}")
    private String apiKey;

    @Nonnull
    @Override
    protected String getCurrencyData() {
        var currenciesText = Stream.of(Currency.values())
                .map(Enum::name)
                .collect(Collectors.joining(","));
        try {
            return currencyFreaksClient.getLatestCurrenciesRates(apiKey, Currency.BASE_CURRENCY.name(), currenciesText);
        } catch (Exception ex) {
            log.error("Unable to get latest currencies rates", ex);
            throw new UnableToUpdateCurrenciesException("Unable to get latest currencies rates", ex);
        }
    }

    @Nonnull
    @Override
    protected Map<Currency, BigDecimal> parseCurrencyData(final String currencyData) {
        var currencyDTO = jsonConverter.convertToObj(currencyData, CurrencyFreakDTO.class);
        return currencyDTO.rates();
    }

}
