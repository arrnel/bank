package com.arrnel.payment.data.repository;

import com.arrnel.payment.data.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentEntity, Long> {
}
