package com.arrnel.tests.service.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.List;

@Slf4j
public abstract class KafkaListener implements Runnable {

    private final Consumer<String, String> consumer;

    private final List<String> topics;
    private boolean isRun = false;

    protected KafkaListener(Consumer<String, String> kafkaConsumer, String topic) {
        this.consumer = kafkaConsumer;
        this.topics = List.of(topic);
    }

    @Override
    public void run() {
        isRun = true;
        consumer.subscribe(topics);
        while (isRun) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                var topic = consumerRecord.topic();
                var requestId = consumerRecord.key();
                try {
                    process(consumerRecord);
                } catch (Exception ex) {
                    log.error("Unable to process record from topic = [{}] with id = [{}]: {}", topic, requestId, ex.getMessage());
                }
            }
        }
    }

    protected abstract void process(ConsumerRecord<String, String> consumerRecord);

    public void shutdown() {
        isRun = false;
    }

}
