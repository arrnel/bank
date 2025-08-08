package com.arrnel.currency.controller;

import com.arrnel.currency.data.enums.Currency;
import com.arrnel.currency.model.CurrencyRateResponseDTO;
import com.arrnel.currency.service.CurrencyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping({"/internal/currency", "/internal/currency/"})
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/rate")
    public CurrencyRateResponseDTO getCurrencyRate(
            @Valid @NotNull(message = "{validation.currency.from.not_null}") @RequestParam(name = "from") Currency from,
            @Valid @NotNull(message = "{validation.currency.to.not_null}") @RequestParam(name = "to") Currency to,
            @RequestParam(required = false) BigDecimal amount
    ) {
        return currencyService.getCurrencyRate(from, to, amount);
    }

}
