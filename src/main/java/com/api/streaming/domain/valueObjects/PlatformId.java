package com.api.streaming.domain.valueObjects;

public record PlatformId( String value) {
    public PlatformId {
        if (value == null ) {
            throw new IllegalArgumentException("El ID de la plataforma no es válido");
        }
    }
}
