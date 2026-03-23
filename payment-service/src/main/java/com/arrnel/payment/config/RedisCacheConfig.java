package com.arrnel.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Value("${app.redis.cache-ttl}")
    private Long cacheTtl;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(cacheTtl))
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("currency_rates", defaultConfig)
                .transactionAware()
                .build();
    }
}

