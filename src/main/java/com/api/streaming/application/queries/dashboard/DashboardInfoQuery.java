package com.api.streaming.application.queries.dashboard;

import java.util.List;

import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;


public record DashboardInfoQuery(
    Long totalAccounts,
    Long totalOrders,
    Long totalPlatforms,
    Double totalOrderMoney,
    List<AccountsByPlatformQuery> accountsByPlatform,
    List<MembershipNearToExpireQuery> membershipNearToExpire,
    List<OrderDetailsSnapshotQuery> ordersNearToExpireQuery
    // Total accounts by platform
) {}
