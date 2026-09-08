package com.api.streaming.application.queries.dashboard;

import java.time.LocalDate;

public record MembershipNearToExpireQuery(
    String email,
    LocalDate membershipExpireDate,
    boolean isFreeTrial
) {}
