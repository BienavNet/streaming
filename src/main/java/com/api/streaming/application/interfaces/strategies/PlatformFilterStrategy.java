package com.api.streaming.application.interfaces.strategies;

import java.util.List;

import com.api.streaming.domain.models.Platform;

public interface PlatformFilterStrategy {
    List<Platform> filter(String criteria);
}
