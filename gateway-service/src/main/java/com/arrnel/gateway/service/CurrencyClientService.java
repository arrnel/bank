package com.arrnel.gateway.service;

import com.arrnel.gateway.client.rest.CurrencyClient;
import com.arrnel.gateway.ex.InternalServiceNotFoundException;
import com.arrnel.gateway.ex.InternalServiceUnknownException;
import com.arrnel.gateway.model.dto.CurrencyRateDTO;
import com.arrnel.gateway.model.enums.Currency;
import com.arrnel.gateway.util.JsonConverter;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class CurrencyClientService {

    private final CurrencyClient currencyClient;
    private final JsonConverter jsonConverter;

    @Nonnull
    public CurrencyRateDTO getCurrencyRate(final Currency from,
                                           final Currency to,
                                           @Nullable final BigDecimal amount
    ) {
        var response = currencyClient.getCurrencyRate(from, to, amount);
        var statusCode = Objects.requireNonNull(HttpStatus.resolve(response.getStatusCode().value()));
        return switch (statusCode) {
            case OK -> Objects.requireNonNull(response.getBody());
            case NOT_FOUND -> {
                log.error("Not found currency(-es) from request");
                throw new InternalServiceNotFoundException(
                        jsonConverter.convertToJson(Objects.requireNonNull(response.getBody()))
                );
            }
            default -> {
                log.error("Unknown status code or internal currency service error");
                throw new InternalServiceUnknownException("Currency service returned body: %s".formatted(response.getBody()));
            }
        };
    }

}
