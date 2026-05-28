package com.api.streaming.domain.valueObjects;

public record AccountId(String value) {
    public AccountId {
        if (value == null) {
            throw new IllegalArgumentException("El ID de la cuenta no es valido");
        }
    }
}
