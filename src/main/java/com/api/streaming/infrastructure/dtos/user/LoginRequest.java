package com.api.streaming.infrastructure.dtos.user;

public record LoginRequest(
    String username,
    String password
) {}
