package com.arrnel.tests.jupiter.extension;

import com.arrnel.tests.config.di.ServiceConfig;
import com.arrnel.tests.service.listener.KafkaListener;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ParametersAreNonnullByDefault
public class KafkaExtension implements SuiteExtension {

    private static final ServiceConfig SERVICE_CONFIG = ServiceConfig.getInstance();
    private static final KafkaListener KAFKA_OPERATION_LISTENER = SERVICE_CONFIG.getKafkaOperationListener();
    private static final KafkaListener KAFKA_OPERATION_RESULT_LISTENER = SERVICE_CONFIG.getKafkaOperationResultListener();
    private static final ExecutorService operationExecutor = Executors.newSingleThreadExecutor();
    private static final ExecutorService operationResultExecutor = Executors.newSingleThreadExecutor();

    @SneakyThrows
    @Override
    public void beforeSuite(ExtensionContext context) {
        operationExecutor.execute(KAFKA_OPERATION_LISTENER);
        operationExecutor.shutdown();
        operationResultExecutor.execute(KAFKA_OPERATION_RESULT_LISTENER);
        operationResultExecutor.shutdown();
    }

    @Override
    public void afterSuite() {
        KAFKA_OPERATION_LISTENER.shutdown();
        KAFKA_OPERATION_RESULT_LISTENER.shutdown();
    }

}
