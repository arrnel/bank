package com.bank.tests.config.di;

import com.bank.tests.client.PaymentApiClient;
import com.bank.tests.config.Config;
import com.bank.tests.service.kafka.KafkaProducer;
import com.bank.tests.service.kafka.KafkaStore;
import com.bank.tests.service.kafka.PaymentKafkaService;
import com.bank.tests.service.kafka.listener.KafkaListener;
import com.bank.tests.service.rest.PaymentApiService;
import com.bank.tests.util.JsonConverter;
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
