package com.api.streaming.application.commands;

import com.api.streaming.domain.valueObjects.PlatformId;

public record UpdatePlatformCommand(
    PlatformId id,
    String name,
    String link,
    String icon
) {}
