package com.bank.payment.service.handler.impl;

import com.bank.payment.controller.kafka.OperationResultProducer;
import com.bank.payment.model.dto.CreateRefundRequestDTO;
import com.bank.payment.service.RefundService;
import com.bank.payment.service.handler.OperationHandler;
import com.bank.payment.util.JsonConverter;
import com.bank.payment.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.bank.payment.data.enums.OperationStatus.SUCCESS;
import static com.bank.payment.model.enums.OperationType.REFUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefundHandler implements OperationHandler {

    private final RefundService refundService;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateRefundRequestDTO.class);
        validationService.validate(
                request,
                CreateRefundRequestDTO.class.getSimpleName()
        );

        var response = refundService.addRefund(request);

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                REFUND,
                SUCCESS,
                jsonConverter.convertToJson(response)
        );

    }

}
