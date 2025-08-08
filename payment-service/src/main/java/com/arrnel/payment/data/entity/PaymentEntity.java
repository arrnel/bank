package com.arrnel.payment.data.entity;

import com.arrnel.payment.data.enums.Currency;
import com.arrnel.payment.data.enums.OperationStatus;
import com.arrnel.payment.data.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(schema = "app", name = "payments")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
public class PaymentEntity extends BaseEntity<PaymentEntity> {

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType paymentType;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", referencedColumnName = "id", nullable = false, updatable = false)
    private CurrencyWalletEntity source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private CurrencyWalletEntity destination;

    @ToString.Include
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ToString.Include
    @Column(name = "comment", nullable = false)
    private String comment;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OperationStatus status;

    @ToString.Include
    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RefundEntity> refunds;

}
