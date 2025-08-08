package com.arrnel.payment.service;

import com.arrnel.payment.data.entity.PaymentEntity;
import com.arrnel.payment.data.repository.PaymentTransactionRepository;
import com.arrnel.payment.service.client.currency.CurrencyClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class PaymentService {

    private final CurrencyClientService currencyClientService;
    private final CurrencyWalletService currencyWalletService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @Transactional
    public PaymentEntity save(PaymentEntity payment) {
        return paymentTransactionRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Optional<PaymentEntity> findById(Long id) {
        return paymentTransactionRepository.findById(id);
    }


}
