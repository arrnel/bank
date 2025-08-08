package com.arrnel.payment.service;

import com.arrnel.payment.data.entity.RefundEntity;
import com.arrnel.payment.data.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;

    public RefundEntity save(RefundEntity entity) {
        return refundRepository.save(entity);
    }

    public Optional<RefundEntity> getById(Long id) {
        return refundRepository.findById(id);
    }

}
