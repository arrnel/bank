package com.arrnel.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;

@Slf4j
@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class JsonConverter {

    private final ObjectMapper objectMapper;

    @Nonnull
    public <T> T convertToObj(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException ex) {
            log.error("Unable to deserialize {}", clazz.getSimpleName(), ex);
            throw new RuntimeException(ex);
        }
    }

    @Nonnull
    public String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Unable to serialize {}", object, ex);
            throw new RuntimeException(ex);
        }
    }

}
