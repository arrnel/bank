package com.arrnel.tests.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Builder
public record ApiErrorDTO(

        @JsonProperty("apiVersion")
        String apiVersion,

        @JsonProperty("error")
        Error error

) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorDTO that = (ApiErrorDTO) o;
        return Objects.equals(error, that.error) && Objects.equals(apiVersion, that.apiVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiVersion, error);
    }

    public record Error(

            @JsonProperty("code")
            String code,

            @JsonProperty("message")
            String message,

            @JsonProperty("errors")
            List<ErrorItem> errors

    ) implements Serializable {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Error error = (Error) o;
            return Objects.equals(code, error.code) && Objects.equals(message, error.message) && Objects.equals(errors, error.errors);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, message, errors);
        }

    }

    public record ErrorItem(

            @JsonProperty("domain")
            String domain,

            @JsonProperty("reason")
            String reason,

            @JsonProperty("message")
            String itemMessage

    ) implements Serializable {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ErrorItem errorItem = (ErrorItem) o;
            return Objects.equals(domain, errorItem.domain) && Objects.equals(reason, errorItem.reason) && Objects.equals(itemMessage, errorItem.itemMessage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(domain, reason, itemMessage);
        }

    }

}