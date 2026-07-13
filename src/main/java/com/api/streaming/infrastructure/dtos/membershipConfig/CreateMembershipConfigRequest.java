package com.api.streaming.infrastructure.dtos.membershipConfig;


public record CreateMembershipConfigRequest(
    String platform,
    String plan,
    int profiles
) {}
