package com.arrnel.payment.data.repository;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyWalletRepository extends JpaRepository<CurrencyWalletEntity, Long> {
}
