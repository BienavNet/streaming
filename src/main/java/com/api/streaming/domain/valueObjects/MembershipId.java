package com.api.streaming.domain.valueObjects;

public record MembershipId(String value) {
    public MembershipId {
        if (value == null || value.isBlank() || value.contains(" ")) {
            throw new IllegalArgumentException("This is a invalid Id for creating this membership.");
        }
    }
}
