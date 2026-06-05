package com.api.streaming.infrastructure.dtos;

import java.time.LocalDateTime;

public record ErrorDetails(
    Integer code,
    String message,
    LocalDateTime timestamp
) {}
