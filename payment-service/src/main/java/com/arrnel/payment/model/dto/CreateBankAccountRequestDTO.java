package com.arrnel.payment.model.dto;

import com.arrnel.payment.validation.anno.ValidBankAccount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@ValidBankAccount
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBankAccountRequestDTO(

        @NotNull(message = "{validation.bank_account.user.not_null}")
        @Positive(message = "{validation.bank_account.user.positive}")
        @JsonProperty("user_id")
        Long userId

) implements Serializable {
}
