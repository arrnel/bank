package com.bank.payment.util;

import com.bank.payment.ex.OperationProcessingException;
import com.bank.payment.model.enums.OperationType;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionOperationComponent {

    private final TransactionTemplate txTemplate;

    @Nonnull
    public <T> T doInTransaction(OperationType operationType, Supplier<T> action) {
        return Objects.requireNonNull(
                txTemplate.execute(status -> {
                    try {
                        return action.get();
                    } catch (Exception ex) {
                        status.setRollbackOnly();
                        throw new OperationProcessingException("Failed to process %s operation".formatted(operationType.name()), ex);
                    }
                }));
    }

}
