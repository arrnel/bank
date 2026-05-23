package com.bank.payment.data.repository;

import com.bank.payment.data.entity.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
}
