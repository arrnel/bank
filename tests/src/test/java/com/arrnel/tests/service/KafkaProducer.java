package com.arrnel.tests.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.concurrent.Future;

@ParametersAreNonnullByDefault
public class KafkaProducer implements AutoCloseable {

    public final org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;

    public KafkaProducer(org.apache.kafka.clients.producer.KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    public Future<RecordMetadata> sendMessage(
            String topic,
            String key,
            Map<String, String> headers,
            String message
    ) {
        ProducerRecord<String, String> kafkaRecord = new ProducerRecord<>(topic, key, message);
        headers.forEach((headerKey, headerValue) ->
                kafkaRecord.headers().add(new RecordHeader(headerKey, headerValue.getBytes()))
        );
        return producer.send(kafkaRecord);
    }

    @Override
    public void close() {
        producer.close();
    }

}
