package com.api.streaming.domain.valueObjects;

public record OrderId(String value) {
    public OrderId {
        if (value == null || value.isBlank() || value.contains(" ")) {
            throw new IllegalArgumentException("This is a invalid Id for creating this order.");
        }
    }   
}
