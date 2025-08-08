package com.arrnel.payment.service;

import com.arrnel.payment.data.entity.BankAccountEntity;
import com.arrnel.payment.data.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountEntity save(BankAccountEntity entity) {
        return bankAccountRepository.save(entity);
    }

    public Optional<BankAccountEntity> findById(Long id) {
        return bankAccountRepository.findById(id);
    }

    public boolean existsByUserId(Long userId) {
        return bankAccountRepository.existsByUserId(userId);
    }
}
