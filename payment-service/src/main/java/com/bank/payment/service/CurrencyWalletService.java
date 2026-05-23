package com.bank.payment.service;

import com.bank.payment.data.entity.CurrencyWalletEntity;
import com.bank.payment.data.repository.CurrencyWalletRepository;
import com.bank.payment.mapper.CurrencyWalletMapper;
import com.bank.payment.model.dto.CreateCurrencyWalletRequestDTO;
import com.bank.payment.model.dto.CreateOperationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bank.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyWalletService {

    public static final String SOURCE_CURRENCY_WALLET = "source";
    public static final String DESTINATION_CURRENCY_WALLET = "destination";

    private final BankAccountService bankAccountService;
    private final CurrencyWalletRepository currencyWalletRepository;
    private final CurrencyWalletMapper currencyWalletMapper;

    @Nonnull
    @Transactional
    public CurrencyWalletEntity save(CurrencyWalletEntity currencyWalletEntity) {
        return currencyWalletRepository.save(currencyWalletEntity);
    }

    @Nonnull
    @Transactional
    public List<CurrencyWalletEntity> saveAll(List<CurrencyWalletEntity> currencyWalletEntities) {
        return currencyWalletRepository.saveAll(currencyWalletEntities);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Optional<CurrencyWalletEntity> findById(Long id) {
        return currencyWalletRepository.findById(id);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public List<CurrencyWalletEntity> findAllByIds(List<Long> ids) {
        return currencyWalletRepository.findAllById(ids);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Map<String, CurrencyWalletEntity> findSourceAndDestinationCw(Long sourceCwId, Long destinationCwId) {
        var cwMap = new HashMap<String, CurrencyWalletEntity>();
        currencyWalletRepository.findAllById(List.of(sourceCwId, destinationCwId))
                .forEach(cw -> {
                    if (cw.getId().equals(sourceCwId)) {
                        cwMap.put(CurrencyWalletService.SOURCE_CURRENCY_WALLET, cw);
                    } else if (cw.getId().equals(destinationCwId)) {
                        cwMap.put(CurrencyWalletService.DESTINATION_CURRENCY_WALLET, cw);
                    }
                });
        return cwMap;
    }

    @Transactional
    public CreateOperationResponseDTO create(CreateCurrencyWalletRequestDTO request) {
        var bankAccount = bankAccountService.findById(request.bankAccountId()).orElse(null);
        var currencyWallet = currencyWalletMapper.toEntity(request, bankAccount);
        log.info("Creating currency wallet: {}", currencyWallet);
        return currencyWalletMapper.toCreateResponseDTO(
                save(currencyWallet),
                SUCCESS
        );
    }
}
