package com.arrnel.tests.config.di;

import com.arrnel.tests.client.PaymentApiClient;
import com.arrnel.tests.config.Config;
import com.arrnel.tests.service.kafka.KafkaProducer;
import com.arrnel.tests.service.kafka.KafkaStore;
import com.arrnel.tests.service.kafka.PaymentKafkaService;
import com.arrnel.tests.service.kafka.listener.KafkaListener;
import com.arrnel.tests.service.rest.PaymentApiService;
import com.arrnel.tests.util.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;

public interface ServiceConfig {

    Config CFG = Config.getInstance();

    static ServiceConfig getInstance() {
        var testEnv = System.getenv("TEST_ENV").toLowerCase();
        return switch (testEnv) {
            case "local", "docker" -> LocalServiceConfig.INSTANCE;
            default -> throw new IllegalStateException("Unknown test environment: %s".formatted(testEnv));
        };
    }

    @Nonnull
    ObjectMapper getObjectMapper();

    @Nonnull
    JsonConverter getJsonConverter();

    @Nonnull
    KafkaStore getKafkaStore();

    @Nonnull
    KafkaProducer getKafkaProducer();

    @Nonnull
    KafkaListener getKafkaOperationListener();

    @Nonnull
    KafkaListener getKafkaOperationResultListener();

    @Nonnull
    PaymentKafkaService getPaymentKafkaService();

    @Nonnull
    PaymentApiClient getPaymentApiClient();

    @Nonnull
    PaymentApiService getPaymentApiService();

}
