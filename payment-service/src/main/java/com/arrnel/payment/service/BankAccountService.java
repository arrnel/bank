package com.arrnel.payment.service;

import com.arrnel.payment.data.entity.BankAccountEntity;
import com.arrnel.payment.data.repository.BankAccountRepository;
import com.arrnel.payment.mapper.BankAccountMapper;
import com.arrnel.payment.model.dto.CreateBankAccountRequestDTO;
import com.arrnel.payment.model.dto.CreateOperationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

import static com.arrnel.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    @Nonnull
    public BankAccountEntity save(BankAccountEntity entity) {
        return bankAccountRepository.save(entity);
    }

    @Nonnull
    @Transactional
    public CreateOperationResponseDTO create(CreateBankAccountRequestDTO request) {
        var bankAccountEntity = bankAccountMapper.toEntity(request);
        log.info("Creating bank account: {}", bankAccountEntity);
        return bankAccountMapper.toCreateResponseDTO(
                save(bankAccountEntity),
                SUCCESS
        );
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Optional<BankAccountEntity> findById(Long id) {
        return bankAccountRepository.findById(id);
    }

    public boolean existsByUserId(Long userId) {
        return bankAccountRepository.existsByUserId(userId);
    }
}
