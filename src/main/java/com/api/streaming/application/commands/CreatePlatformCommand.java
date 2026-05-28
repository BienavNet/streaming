package com.api.streaming.application.commands;

public record CreatePlatformCommand(
    String name,
    String link,
    String icon
) {}
