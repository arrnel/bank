package com.arrnel.currency.service;

import com.arrnel.currency.data.entity.CurrencyEntity;
import com.arrnel.currency.data.enums.Currency;
import com.arrnel.currency.data.repository.CurrencyRepository;
import com.arrnel.currency.ex.CurrenciesIsAlreadyUpdatingException;
import com.arrnel.currency.ex.CurrencyNotFoundException;
import com.arrnel.currency.ex.UnableToUpdateCurrenciesException;
import com.arrnel.currency.model.CurrencyRateResponseDTO;
import com.arrnel.currency.service.client.currency.CurrencyClientService;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.math.RoundingMode.CEILING;
import static java.util.stream.Collectors.toCollection;

@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyClientService currencyClientService;

    private static final AtomicBoolean isCurrenciesUpdating = new AtomicBoolean(false);

    @PostConstruct
    public void init() {

        log.info("Checking currencies table for update.");

        TreeSet<String> currencyNamesFromDB = currencyRepository.findAll().stream()
                .map(currencyEntity -> currencyEntity.getCurrency().name().toUpperCase())
                .collect(toCollection(TreeSet::new));

        TreeSet<String> currencyNamesFromEnum = Arrays.stream(Currency.values())
                .map(currency -> currency.name().toUpperCase())
                .collect(Collectors.toCollection(TreeSet::new));


        if (!currencyNamesFromEnum.equals(currencyNamesFromDB))
            update();

    }

    @Transactional
    public void saveAll(List<CurrencyEntity> currencies) {
        currencyRepository.saveAll(currencies);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public List<CurrencyEntity> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Nonnull
    @Transactional(readOnly = true)
    public CurrencyRateResponseDTO getCurrencyRate(final Currency from,
                                                   final Currency to,
                                                   @Nullable final BigDecimal amount
    ) {

        var fromRate = currencyRepository.findByCurrency(from)
                .orElseThrow(() -> new CurrencyNotFoundException(from))
                .getRate();

        var toRate = currencyRepository.findByCurrency(to)
                .orElseThrow(() -> new CurrencyNotFoundException(to))
                .getRate();

        var rate = amount == null
                ? toRate.divide(fromRate, 6, CEILING)
                : toRate.divide(fromRate, 6, CEILING).multiply(amount);

        return new CurrencyRateResponseDTO(from, to, rate);

    }

    public void update() {

        log.info("Updating currencies exchange rates");

        if (isCurrenciesUpdating.get())
            throw new CurrenciesIsAlreadyUpdatingException("Currencies is already updating exception");

        try {
            isCurrenciesUpdating.set(true);
            currencyClientService.updateCurrenciesRate();
            isCurrenciesUpdating.set(false);
        } catch (Exception ex) {
            isCurrenciesUpdating.set(false);
            throw new UnableToUpdateCurrenciesException("Unable to update currencies rate exception", ex);
        }


        log.info("Successfully updated currency table");

    }

}
