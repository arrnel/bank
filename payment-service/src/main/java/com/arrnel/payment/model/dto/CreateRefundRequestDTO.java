package com.arrnel.payment.model.dto;

import com.arrnel.payment.validation.anno.ValidRefund;
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
@ValidRefund
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateRefundRequestDTO(

        @NotNull(message = "{validation.refund.payment.not_null}")
        @JsonProperty("transfer_id")
        Long transferId,

        @NotNull(message = "{validation.refund.amount.not_null}")
        @Positive(message = "{validation.refund.amount.positive}")
        @Digits(integer = 19, fraction = 2, message = "{validation.refund.amount.digits}")
        @JsonProperty("amount")
        BigDecimal amount,

        @Size(min = 10, max = 2000, message = "{validation.refund.reason.size}")
        @JsonProperty("reason")
        String reason

) implements Serializable {

}
