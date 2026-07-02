package com.api.streaming.application.queries;

import java.time.LocalDate;

import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipId;

public record MembershipDetailsQuery(
    MembershipId id,
    MembershipConfig details,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate
) {}
