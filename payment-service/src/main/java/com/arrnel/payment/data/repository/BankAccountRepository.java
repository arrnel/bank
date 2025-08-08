package com.arrnel.payment.data.repository;

import com.arrnel.payment.data.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    boolean existsByUserId(Long userId);
}
