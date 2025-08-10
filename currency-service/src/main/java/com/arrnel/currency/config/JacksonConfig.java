package com.arrnel.currency.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES)
                .build()
                .registerModule(new JavaTimeModule())
                .findAndRegisterModules();
    }

}
