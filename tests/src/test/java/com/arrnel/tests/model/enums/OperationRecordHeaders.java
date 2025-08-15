package com.arrnel.tests.model.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OperationRecordHeaders {

    OPERATION_TYPE("operation_type"), OPERATION_STATUS("operation_status");
    private final String value;

    @Override
    public String toString() {
        return value;
    }

}
