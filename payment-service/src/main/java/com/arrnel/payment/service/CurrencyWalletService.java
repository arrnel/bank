package com.arrnel.payment.service;

import com.arrnel.payment.data.entity.CurrencyWalletEntity;
import com.arrnel.payment.data.repository.CurrencyWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyWalletService {

    public static final String SOURCE_CURRENCY_WALLET = "source";
    public static final String DESTINATION_CURRENCY_WALLET = "destination";

    private final CurrencyWalletRepository currencyWalletRepository;

    @Transactional
    public CurrencyWalletEntity save(CurrencyWalletEntity currencyWalletEntity) {
        return currencyWalletRepository.save(currencyWalletEntity);
    }

    @Transactional
    public List<CurrencyWalletEntity> saveAll(List<CurrencyWalletEntity> currencyWalletEntities) {
        return currencyWalletRepository.saveAll(currencyWalletEntities);
    }

    @Transactional(readOnly = true)
    public Optional<CurrencyWalletEntity> findById(Long id) {
        return currencyWalletRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CurrencyWalletEntity> findAllByIds(List<Long> ids) {
        return currencyWalletRepository.findAllById(ids);
    }

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

}
