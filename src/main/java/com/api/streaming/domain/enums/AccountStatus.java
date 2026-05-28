package com.api.streaming.domain.enums;

public enum AccountStatus {
    ACTIVE("active"),
    DEACTIVE("deactive");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AccountStatus fromValue(String value) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.value.equalsIgnoreCase(value))
                return status;
        }
        throw new IllegalArgumentException("Account Status is not valid.");
    }
}
