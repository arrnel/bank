package com.arrnel.payment.data.repository;

import com.arrnel.payment.data.entity.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
}
