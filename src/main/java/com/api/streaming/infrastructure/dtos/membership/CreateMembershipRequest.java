package com.api.streaming.infrastructure.dtos.membership;

import java.time.LocalDate;

public record CreateMembershipRequest(
    String account,
    String membershipConfig,
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate
) {}
