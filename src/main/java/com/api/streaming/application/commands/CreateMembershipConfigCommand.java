package com.api.streaming.application.commands;

import com.api.streaming.domain.valueObjects.PlatformId;

public record CreateMembershipConfigCommand(
    PlatformId platform,
    String plan,
    int profiles
) {}
