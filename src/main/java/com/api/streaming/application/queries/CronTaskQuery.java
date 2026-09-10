package com.api.streaming.application.queries;

public record CronTaskQuery(
    String id,
    String name,
    String description,
    boolean active
) {}
