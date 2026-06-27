package com.api.streaming.domain.enums;

public enum OrderType {
    FULL("full"),
    PROFILE("profile");

    private final String value;

    OrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderType fromValue(String value) {
        for (OrderType status : OrderType.values()) {
            if (status.value.equalsIgnoreCase(value))
                return status;
        }
        throw new IllegalArgumentException("Order Type is not valid.");
    }
}
