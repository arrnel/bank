package com.arrnel.payment.service.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface OperationHandler {

    @Transactional
    void process(final String requestId, final String message);

}
