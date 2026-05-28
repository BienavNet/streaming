package com.api.streaming.application.useCases.Platform;

import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetPlatformUseCase {
    
    private final PlatformInterface platformInterface;

    public Platform execute(PlatformId id) {
        return this.platformInterface.getPlatform(id)
            .orElseThrow(() -> new EntityNotFoundException("Platform with that ID not found."));
    }
}
