package com.arrnel.tests.config.di;

import com.arrnel.tests.client.PaymentApiClient;
import com.arrnel.tests.service.kafka.KafkaProducer;
import com.arrnel.tests.service.kafka.KafkaStore;
import com.arrnel.tests.service.kafka.PaymentKafkaService;
import com.arrnel.tests.service.kafka.listener.KafkaOperationListener;
import com.arrnel.tests.service.kafka.listener.KafkaOperationResultListener;
import com.arrnel.tests.service.rest.PaymentApiService;
import com.arrnel.tests.util.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.KafkaConsumer;

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

    @Nonnull
    @Override
    public PaymentKafkaService getPaymentKafkaService() {
        return new PaymentKafkaService(getKafkaProducer(), getKafkaStore(), getJsonConverter());
    }

    @Nonnull
    @Override
    public PaymentApiClient getPaymentApiClient() {
        return new PaymentApiClient(getObjectMapper());
    }

    @Nonnull
    @Override
    public PaymentApiService getPaymentApiService() {
        return new PaymentApiService(getPaymentApiClient());
    }

}
