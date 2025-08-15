package com.arrnel.tests.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateTransferRequestDTO(

        @JsonProperty("source_id")
        Long sourceId,

        @JsonProperty("destination_id")
        Long destinationId,

        @JsonProperty("amount")
        BigDecimal amount,

        @JsonProperty("comment")
        String comment

) implements Serializable {

}
