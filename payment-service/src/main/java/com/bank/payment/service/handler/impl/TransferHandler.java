package com.bank.payment.service.handler.impl;

import com.bank.payment.controller.kafka.OperationResultProducer;
import com.bank.payment.model.dto.CreateTransferRequestDTO;
import com.bank.payment.model.enums.OperationType;
import com.bank.payment.service.PaymentService;
import com.bank.payment.service.handler.OperationHandler;
import com.bank.payment.util.JsonConverter;
import com.bank.payment.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.bank.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferHandler implements OperationHandler {

    private final PaymentService paymentService;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateTransferRequestDTO.class);
        validationService.validate(
                request,
                CreateTransferRequestDTO.class.getSimpleName()
        );

        var response = paymentService.transfer(request);

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                OperationType.TRANSFER,
                SUCCESS,
                jsonConverter.convertToJson(response)
        );
    }

}
