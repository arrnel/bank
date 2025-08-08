package com.arrnel.currency.data.entity;

import com.arrnel.currency.data.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(schema = "app", name = "currency_rates")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
public class CurrencyEntity extends BaseEntity<CurrencyEntity> {

    @Enumerated(EnumType.STRING)
    @Column(name = "title", nullable = false, unique = true, updatable = false)
    private Currency currency;

    @ToString.Include
    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

}
