package com.bank.payment.data.repository;

import com.bank.payment.data.entity.CurrencyWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyWalletRepository extends JpaRepository<CurrencyWalletEntity, Long> {
}
