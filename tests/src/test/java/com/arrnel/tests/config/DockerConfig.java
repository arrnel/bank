package com.arrnel.tests.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.Nonnull;
import java.util.Properties;
import java.util.UUID;

public enum DockerConfig implements Config {

    INSTANCE;

    @Nonnull
    @Override
    public String kafkaAddress() {
        return "kafka:9092";
    }

    @Nonnull
    @Override
    public String kafkaConsumerGroupId() {
        return "test-" + UUID.randomUUID();
    }

    @Nonnull
    @Override
    public Properties kafkaConsumerProperties() {
        var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaAutoOffsetReset());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
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
