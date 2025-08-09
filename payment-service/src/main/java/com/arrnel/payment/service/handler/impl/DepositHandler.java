package com.arrnel.payment.service.handler.impl;

import com.arrnel.payment.controller.kafka.OperationResultProducer;
import com.arrnel.payment.model.dto.CreateDepositRequestDTO;
import com.arrnel.payment.model.enums.OperationType;
import com.arrnel.payment.service.PaymentService;
import com.arrnel.payment.service.handler.OperationHandler;
import com.arrnel.payment.util.JsonConverter;
import com.arrnel.payment.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.arrnel.payment.data.enums.OperationStatus.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositHandler implements OperationHandler {

    private final PaymentService paymentService;
    private final JsonConverter jsonConverter;
    private final ValidationService validationService;
    private final OperationResultProducer operationResultProducer;

    @Override
    public void process(String requestId, String message) {
        var request = jsonConverter.convertToObj(message, CreateDepositRequestDTO.class);
        validationService.validate(
                requestId,
                OperationType.DEPOSIT,
                request,
                CreateDepositRequestDTO.class.getSimpleName()
        );

        var response = paymentService.addDeposit(request);

        operationResultProducer.produceResult(
                OperationResultProducer.OPERATION_RESULT_TOPIC,
                requestId,
                OperationType.DEPOSIT,
                SUCCESS,
                jsonConverter.convertToJson(response)
        );
    }

}
