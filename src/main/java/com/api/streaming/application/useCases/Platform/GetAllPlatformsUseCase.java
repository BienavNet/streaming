package com.api.streaming.application.useCases.Platform;

import java.util.List;

import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAllPlatformsUseCase {
    private final PlatformInterface platformInterface;

    public List<Platform> execute() {
        List<Platform> platforms = this.platformInterface.getAllPlatforms();
        return platforms;
    }
}
