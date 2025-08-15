package com.arrnel.tests.config.di;

import com.arrnel.tests.client.GatewayApiClient;
import com.arrnel.tests.service.KafkaProducer;
import com.arrnel.tests.service.KafkaStore;
import com.arrnel.tests.service.PaymentKafkaService;
import com.arrnel.tests.service.listener.KafkaOperationListener;
import com.arrnel.tests.service.listener.KafkaOperationResultListener;
import com.arrnel.tests.util.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public enum LocalServiceConfig implements ServiceConfig {

    INSTANCE;

    @Nonnull
    @Override
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    @Nonnull
    @Override
    public JsonConverter getJsonConverter() {
        return new JsonConverter(getObjectMapper());
    }

    @Nonnull
    @Override
    public KafkaStore getKafkaStore() {
        return KafkaStore.INSTANCE;
    }

    @Nonnull
    @Override
    public KafkaProducer getKafkaProducer() {
        return new KafkaProducer(new org.apache.kafka.clients.producer.KafkaProducer<>(CFG.kafkaProducerProperties()));
    }

    @Nonnull
    @Override
    public KafkaOperationListener getKafkaOperationListener() {
        return new KafkaOperationListener(
                new KafkaConsumer<>(CFG.kafkaConsumerProperties()),
                getJsonConverter(),
                "operation-topic"
        );
    }

    @Nonnull
    @Override
    public KafkaOperationResultListener getKafkaOperationResultListener() {
        return new KafkaOperationResultListener(
                new KafkaConsumer<>(CFG.kafkaConsumerProperties()),
                getJsonConverter(),
                "operation-result-topic"
        );
    }

    @NotNull
    @Override
    public GatewayApiClient getGatewayApiClient() {
        return new GatewayApiClient();
    }

    @Nonnull
    @Override
    public PaymentKafkaService getPaymentKafkaService() {
        return new PaymentKafkaService(getKafkaProducer(), getKafkaStore(), getJsonConverter());
    }
    
}
