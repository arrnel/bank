package com.arrnel.payment.data.entity;

import com.arrnel.payment.data.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(schema = "app", name = "currency_wallets")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
public class CurrencyWalletEntity extends BaseEntity<CurrencyWalletEntity> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id", nullable = false)
    private BankAccountEntity bankAccount;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @ToString.Include
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(
            mappedBy = "source",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PaymentEntity> payments;

    @ToString.Include
    @Column(name = "error_message")
    private String errorMessage;

}
