package com.api.streaming.infrastructure.dtos.membership;

import java.time.LocalDate;

public record UpdateMembershipRequest(
    boolean isFreeTrial,
    LocalDate membershipBuyDate,
    LocalDate membershipExpireDate,
    String membershipConfigId
) {}
