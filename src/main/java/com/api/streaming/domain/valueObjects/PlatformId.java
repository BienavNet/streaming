package com.api.streaming.domain.valueObjects;

public record PlatformId( String value) {
    public PlatformId {
        if (value == null || value.isBlank() || value.contains(" ") ) {
            throw new IllegalArgumentException("This is a invalid Id for creating this platform.");
        }
    }
}
