package com.api.streaming.infrastructure.dtos.membership;

import java.time.LocalDate;

import com.api.streaming.infrastructure.dtos.membershipConfig.MembershipConfigResponse;

public record MembershipDetailsResponse(
    String id,
    MembershipConfigResponse details,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate
) {}
