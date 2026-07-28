package com.api.streaming.infrastructure.dtos.membershipConfig;


public record MembershipConfigResponse(
    String id,
    String platform,
    String plan,
    Integer profiles
) {}
