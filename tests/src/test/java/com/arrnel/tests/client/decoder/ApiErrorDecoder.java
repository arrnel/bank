package com.arrnel.tests.client.decoder;

import com.arrnel.tests.ex.ApiException;
import com.arrnel.tests.ex.FeignException;
import com.arrnel.tests.model.dto.ApiErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ApiErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public ApiErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try (var body = response.body().asInputStream()) {
            var responseBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
            if (response.status() >= 400 && response.status() <= 599) {
                try {
                    var error = objectMapper.readValue(responseBody, ApiErrorDTO.class);
                    log.error("API error occurred for method [{}]: {}", methodKey, error);
                    return new ApiException(error, response.status());
                } catch (IOException e) {
                    log.error("Failed to deserialize error response for method [{}]: {}", methodKey, responseBody, e);
                    return new FeignException(response.status(), "Failed to parse error response: " + responseBody);
                }
            }
            return new FeignException(response.status(), "Unexpected response status: " + response.status());
        } catch (IOException e) {
            log.error("Failed to read error response body for method [{}]", methodKey, e);
            return new FeignException(response.status(), "Failed to read response body");
        }
    }


}