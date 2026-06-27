package com.api.streaming.domain.valueObjects;

public record MembershipConfigId(String value) {

    public MembershipConfigId {
        if (value == null || value.isBlank() || value.contains(" ")) {
            throw new IllegalArgumentException("This is a invalid Id for creating this membership config.");
        }
    }

}
