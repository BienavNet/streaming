package com.api.streaming.application.queries.dashboard;

import com.api.streaming.application.queries.MembershipDetailsQuery;

public record DashboardInfoQuery(
    int totalAccounts,
    int totalOrders,
    int totalPlatforms,
    int totalOrderMoney,
    AccountsByPlatformQuery[] accountsByPlatform,
    MembershipDetailsQuery[] membershipNearToExpire
    // Total accounts by platform
) {}
