package com.api.streaming.application.queries.dashboard;

import java.util.List;


public record DashboardInfoQuery(
    Long totalAccounts,
    Long totalOrders,
    Long totalPlatforms,
    Double totalOrderMoney,
    List<AccountsByPlatformQuery> accountsByPlatform,
    List<MembershipNearToExpireQuery> membershipNearToExpire
    // Total accounts by platform
) {}
