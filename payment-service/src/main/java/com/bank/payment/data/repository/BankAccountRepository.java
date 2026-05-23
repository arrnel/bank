package com.bank.payment.data.repository;

import com.bank.payment.data.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    boolean existsByUserId(Long userId);
}
