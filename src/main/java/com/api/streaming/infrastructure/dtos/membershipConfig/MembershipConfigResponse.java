package com.api.streaming.infrastructure.dtos.membershipConfig;

import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

public record MembershipConfigResponse(
    MembershipConfigId id,
    PlatformId platform,
    String plan,
    Integer profiles
) {}
