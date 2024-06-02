package org.swp.configuration.constant.service;

public enum ServiceConstantNumber {
    NUMBER_OF_LATEST_SERVICES(6),
    NUMBER_OF_MOST_RCMD_SERVICES(6);
    private final int value;

    ServiceConstantNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
