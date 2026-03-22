package com.arrnel.tests.ex;

import com.arrnel.tests.model.dto.ApiErrorDTO;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ApiErrorDTO error;
    private final int status;

    public ApiException(ApiErrorDTO error, int status) {
        super(error.error().message());
        this.error = error;
        this.status = status;
    }

}