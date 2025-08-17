package com.arrnel.gateway.client.kafka;

import com.arrnel.gateway.ex.IllegalPaymentOperationException;
import com.arrnel.gateway.model.dto.ApiErrorDTO;
import com.arrnel.gateway.model.dto.CreateOperationResponseDTO;
import com.arrnel.gateway.model.enums.PaymentOperationStatus;
import com.arrnel.gateway.model.enums.PaymentOperationType;
import com.arrnel.gateway.util.JsonConverter;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOperationResultConsumer {

    private static final String OPERATION_TYPE_HEADER = "operation_type";
    private static final String OPERATION_STATUS_HEADER = "operation_status";

    private final JsonConverter jsonConverter;
    private final Map<UUID, CompletableFuture<CreateOperationResponseDTO>> pendingRequests = new ConcurrentHashMap<>();

    @KafkaListener(topics = "operation-result-topic", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> kafkaRecord) {

        var requestId = UUID.fromString(kafkaRecord.key());
        var headers = kafkaRecord.headers();
        var status = getHeaderValue(headers, OPERATION_STATUS_HEADER);
        var type = PaymentOperationType.fromString(getHeaderValue(headers, OPERATION_TYPE_HEADER));

        log.info("Get operation result for request with id = [{}] and status = [{}]", requestId, status);

        CompletableFuture<CreateOperationResponseDTO> future = pendingRequests.get(requestId);
        if (future == null) {
            log.warn("Not found pending operation for requestId = [{}]", requestId);
            return;
        }

        var message = kafkaRecord.value();

        if (PaymentOperationStatus.SUCCESS.name().equalsIgnoreCase(status)) {
            var response = jsonConverter.convertToObj(message, CreateOperationResponseDTO.class);
            future.complete(response);
        } else if (PaymentOperationStatus.FAILED.name().equalsIgnoreCase(status)) {
            var error = jsonConverter.convertToObj(message, ApiErrorDTO.class);
            future.completeExceptionally(new IllegalPaymentOperationException(requestId, type, error));
        }
    }

    public void registerOperation(UUID requestId) {
        pendingRequests.put(requestId, new CompletableFuture<>());
    }

    @Nonnull
    public CreateOperationResponseDTO waitForResult(UUID requestId, long timeoutSeconds) {
        try {
            return pendingRequests.get(requestId)
                    .orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .join();
        } catch (CompletionException ex) {
            if (ex.getCause() instanceof RuntimeException re) {
                throw re;
            }
            throw new RuntimeException("Error while waiting operation result", ex);
        } finally {
            pendingRequests.remove(requestId);
        }
    }

    @Nonnull
    private String getHeaderValue(final Headers headers, final String header) {
        return new String(
                headers.lastHeader(header)
                        .value()
        );
    }

}
