package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;

public interface PlatformInterface {
    Optional<Platform> getPlatform(PlatformId platformId);
    Optional<Platform> getByName(String name);
    List<Platform> getAllPlatforms();
    Platform savePlatform(Platform platform);
    void deletePlatform(PlatformId platformId);

    //filters 
    List<Platform> findByName(String name);
}
