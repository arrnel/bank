package com.arrnel.payment.data.entity;

import com.arrnel.payment.data.enums.Currency;
import com.arrnel.payment.data.enums.OperationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(schema = "app", name = "refunds")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
public class RefundEntity extends BaseEntity<RefundEntity> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private PaymentEntity payment;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, updatable = false)
    private Currency currency;

    @ToString.Include
    private BigDecimal amount;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OperationStatus status;

    @ToString.Include
    @Column(name = "error_message")
    private String errorMessage;

}
