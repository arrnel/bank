package com.arrnel.tests.config;


import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public interface Config {

    @Nonnull
    static Config getInstance() {
        var testEnv = System.getenv("TEST_ENV").toLowerCase();
        return switch (testEnv) {
            case "local" -> LocalConfig.INSTANCE;
            case "docker" -> DockerConfig.INSTANCE;
            default -> throw new IllegalStateException("Unknown test environment: %s".formatted(testEnv));
        };
    }

    @Nonnull
    String kafkaAddress();

    @Nonnull
    String kafkaConsumerGroupId();

    @Nonnull
    default String kafkaAutoOffsetReset() {
        return "earliest";
    }

    @Nonnull
    default List<String> kafkaTopics() {
        return List.of(
                "operation-topic",
                "operation-result-topic"
        );
    }

    @Nonnull
    Properties kafkaConsumerProperties();

    @Nonnull
    Properties kafkaProducerProperties();

    @Nonnull
    default Duration kafkaTimeoutBetweenConsumerUpdates() {
        return Duration.ofMillis(100);
    }

    @Nonnull
    default Duration kafkaMaxTimeoutWaitingResult() {
        return Duration.ofSeconds(5);
    }

}
