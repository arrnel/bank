package com.arrnel.gateway.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateTransferRequestDTO(

        @NotNull(message = "{validation.transfer.source.not_null}")
        @Positive(message = "{validation.transfer.source.positive}")
        @JsonProperty("source_id")
        Long sourceId,

        @NotNull(message = "{validation.transfer.destination.not_null}")
        @Positive(message = "{validation.transfer.destination.positive}")
        @JsonProperty("destination_id")
        Long destinationId,

        @NotNull(message = "{validation.transfer.amount.not_null}")
        @Positive(message = "{validation.transfer.amount.positive}")
        @Digits(integer = 19, fraction = 2, message = "{validation.refund.amount.digits}")
        @JsonProperty("amount")
        BigDecimal amount,

        @Size(min = 10, max = 2000, message = "{validation.transfer.comment.size}")
        @JsonProperty("comment")
        String comment

) implements Serializable {
}
