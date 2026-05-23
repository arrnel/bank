package com.bank.currency.data.repository;

import com.bank.currency.data.entity.CurrencyEntity;
import com.bank.currency.data.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {

    @Nonnull
    Optional<CurrencyEntity> findByCurrency(Currency currency);

}
