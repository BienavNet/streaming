package com.api.streaming.domain.valueObjects;

public record PaymentId( String value) {
    public PaymentId {
        if (value == null) {
            throw new IllegalArgumentException("El ID del pago no es válido");
        }
    }
}
