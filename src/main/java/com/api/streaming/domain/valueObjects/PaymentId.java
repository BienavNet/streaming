package com.api.streaming.domain.valueObjects;

public record PaymentId( String value) {
    public PaymentId {
        if (value == null || value.isBlank() || value.contains(" ")) {
            throw new IllegalArgumentException("This is a invalid Id for creating this payment.");
        }
    }
}
