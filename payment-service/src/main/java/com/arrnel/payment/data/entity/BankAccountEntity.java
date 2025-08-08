package com.arrnel.payment.data.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(schema = "app", name = "bank_accounts")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
public class BankAccountEntity extends BaseEntity<BankAccountEntity> {

    @ToString.Include
    @Column(nullable = false, unique = true, updatable = false)
    private Long userId;

    @ToString.Include
    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(
            mappedBy = "bankAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CurrencyWalletEntity> currencyWallets;
}
