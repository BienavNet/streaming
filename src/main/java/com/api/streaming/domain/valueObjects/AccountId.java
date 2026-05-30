package com.api.streaming.domain.valueObjects;

public record AccountId(String value) {
    public AccountId {
        if (value == null || value.isBlank() || value.contains(" ")) {
            throw new IllegalArgumentException("This is a invalid Id for creating this account.");
        }
    }
}
