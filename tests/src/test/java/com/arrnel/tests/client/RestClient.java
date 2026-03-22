package com.arrnel.tests.client;

import com.arrnel.tests.client.api.FeignApi;
import com.arrnel.tests.client.decoder.ApiErrorDecoder;
import com.arrnel.tests.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger.Level;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public abstract class RestClient<T extends FeignApi> {

    protected static final Config CFG = Config.getInstance();
    protected static final Level DEFAULT_LOG_LEVEL = CFG.defaultApiLogLevel();
    protected final T feign;
    protected ObjectMapper objectMapper;

    protected RestClient(Class<T> apiType,
                                              String baseUrl,
                                              ObjectMapper objectMapper
    ) {
        this(apiType, baseUrl, DEFAULT_LOG_LEVEL, objectMapper);
        this.objectMapper = objectMapper;
    }

    protected RestClient(Class<T> apiType,
                                              String baseUrl,
                                              Level logLevel,
                                              ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
        this.feign = Feign.builder()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .errorDecoder(new ApiErrorDecoder(objectMapper))
                .logLevel(logLevel)
                .target(apiType, baseUrl);
    }
}
