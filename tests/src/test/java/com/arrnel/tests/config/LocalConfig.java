package com.arrnel.tests.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.Nonnull;
import java.util.Properties;
import java.util.UUID;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public enum LocalConfig implements Config {

    INSTANCE;

    @Nonnull
    @Override
    public String kafkaAddress() {
        return "localhost:9092";
    }

    @Nonnull
    @Override
    public String kafkaConsumerGroupId() {
        return "test-" + UUID.randomUUID().toString();
    }

    @Nonnull
    @Override
    public Properties kafkaConsumerProperties() {
        var props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaAddress());
        props.put(GROUP_ID_CONFIG, kafkaConsumerGroupId());
        props.put(AUTO_OFFSET_RESET_CONFIG, kafkaAutoOffsetReset());
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        return props;
    }

    @Nonnull
    @Override
    public Properties kafkaProducerProperties() {
        var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

}
