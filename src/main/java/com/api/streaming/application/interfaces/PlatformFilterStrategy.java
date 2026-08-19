package com.api.streaming.application.interfaces;

import java.util.List;

import com.api.streaming.domain.models.Platform;

public interface PlatformFilterStrategy {
    List<Platform> filter(String criteria);
}
