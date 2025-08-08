package com.arrnel.currency.ex.handler;

import com.arrnel.currency.ex.CurrencyNotFoundException;
import com.arrnel.currency.model.ApiErrorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class CurrencyExceptionHandler {

    private static final String CURRENCY_NOT_FOUND_REASON = "validation.currency.not_found.reason";
    private static final String CURRENCY_NOT_FOUND_MESSAGE = "validation.currency.not_found.message";

    private final MessageSource messageSource;

    @Value("${app.api.version}")
    private String apiVersion;

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> currencyNotFoundExceptionHandler(final CurrencyNotFoundException ex,
                                                                        final Locale locale) {
        var currencyName = ex.getCurrency();

        var reason = messageSource.getMessage(
                CURRENCY_NOT_FOUND_REASON,
                new Long[0],
                "Currency not found",
                locale);

        var message = messageSource.getMessage(
                CURRENCY_NOT_FOUND_MESSAGE,
                new String[]{currencyName},
                "Currency [%s] not found".formatted(currencyName),
                locale
        );
        var apiError = ApiErrorDTO.builder()
                .apiVersion(apiVersion)
                .code(HttpStatus.NOT_FOUND.toString())
                .reason(reason)
                .message(message)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE);

        return new ResponseEntity<>(apiError, headers, HttpStatus.NOT_FOUND);
    }

}
