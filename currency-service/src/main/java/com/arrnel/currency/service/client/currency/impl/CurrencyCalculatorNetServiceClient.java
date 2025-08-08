package com.arrnel.currency.service.client.currency.impl;

import com.arrnel.currency.client.CurrencyCalculatorNetClient;
import com.arrnel.currency.data.enums.Currency;
import com.arrnel.currency.data.repository.CurrencyRepository;
import com.arrnel.currency.ex.UnableToUpdateCurrenciesException;
import com.arrnel.currency.service.client.currency.CurrencyClientService;
import com.arrnel.currency.util.JsonConverter;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Primary
@Service
@ParametersAreNonnullByDefault
public class CurrencyCalculatorNetServiceClient extends CurrencyClientService {

    private static final String CURRENCY_PATTERN = "\\[\"(\\w{3})\",(\\d+\\.?\\d*)]";

    private final CurrencyCalculatorNetClient currencyCalculatorNetClient;

    @Autowired
    public CurrencyCalculatorNetServiceClient(CurrencyRepository currencyRepository,
                                              JsonConverter jsonConverter,
                                              CurrencyCalculatorNetClient currencyCalculatorNetClient) {
        super(currencyRepository, jsonConverter);
        this.currencyCalculatorNetClient = currencyCalculatorNetClient;
    }

    @Nonnull
    @Override
    protected String getCurrencyData() {
        return currencyCalculatorNetClient.getHtmlWithCurrencies("1", Currency.USD.name(), Currency.EUR.name());
    }

    @Nonnull
    @Override
    protected Map<Currency, BigDecimal> parseCurrencyData(final String currencyData) {
        Map<Currency, BigDecimal> map = new TreeMap<>();
        var currenciesNames = Stream.of(Currency.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        var matcher = Pattern.compile(CURRENCY_PATTERN)
                .matcher(getVarValue(currencyData));

        while (matcher.find()) {
            var currencyName = matcher.group(1);
            var rate = new BigDecimal(matcher.group(2));
            if (currenciesNames.contains(currencyName)) {
                map.put(Currency.fromString(currencyName), rate);
                currenciesNames.remove(currencyName);
            }
        }

        return map;

    }

    private String getVarValue(String htmlBody) {
        var currenciesVariableName = "listsArrayData";
        if (!htmlBody.contains(currenciesVariableName))
            throw new UnableToUpdateCurrenciesException("Calculator net html not contains variable [%s]"
                    .formatted(currenciesVariableName));

        String varText = currenciesVariableName + " = ";
        htmlBody = htmlBody.substring(htmlBody.indexOf(varText) + varText.length());
        return htmlBody.substring(0, htmlBody.indexOf(";"));
    }
}
