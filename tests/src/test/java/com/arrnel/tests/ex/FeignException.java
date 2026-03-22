package com.arrnel.tests.ex;

import lombok.Getter;

@Getter
public class FeignException extends RuntimeException {

    private final Integer statusCode;

    public FeignException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

}
