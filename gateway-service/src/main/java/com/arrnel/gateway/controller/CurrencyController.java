package com.arrnel.gateway.controller;

import com.arrnel.gateway.model.dto.CurrencyRateDTO;
import com.arrnel.gateway.model.enums.Currency;
import com.arrnel.gateway.service.CurrencyClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping({"/api/v1/currency", "/api/v1/currency/"})
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyClientService currencyService;

    @GetMapping({"/rate", "/rate/"})
    @ResponseStatus(OK)
    public final CurrencyRateDTO getCurrencyRate(@RequestParam("from") final Currency from,
                                                 @RequestParam("to") final Currency to,
                                                 @RequestParam(value = "amount", required = false) final BigDecimal amount
    ) {
        return currencyService.getCurrencyRate(from, to, amount);
    }

}
