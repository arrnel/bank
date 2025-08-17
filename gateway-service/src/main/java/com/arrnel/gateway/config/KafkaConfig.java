package com.arrnel.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Slf4j
@Configuration
@ParametersAreNonnullByDefault
public class KafkaConfig {

    private static final String DEFAULT_GROUP_ID = "default-group-id";
    private static final boolean ENABLE_AUTO_COMMIT = true;

    @Bean
    public ConsumerFactory<String, String> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers) {
        final Map<String, Object> config = new HashMap<>();
        config.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(GROUP_ID_CONFIG, DEFAULT_GROUP_ID);
        config.put(ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(ENABLE_AUTO_COMMIT));
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
            (final ConsumerFactory<String, String> consumerFactory) {

        final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers) {
        final Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(final ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }


}
