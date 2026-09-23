package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.DashboardInfoInterface;
import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;
import com.api.streaming.application.queries.dashboard.AccountsByPlatformQuery;
import com.api.streaming.application.queries.dashboard.DashboardInfoQuery;
import com.api.streaming.application.queries.dashboard.MembershipNearToExpireQuery;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DashboardIRepository implements DashboardInfoInterface {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public DashboardInfoQuery getDetails() {

        Long totalAccounts = this.jdbcTemplate.queryForObject("SELECT COUNT(id) AS totalAccounts FROM account", Long.class);

        Long totalOrders = this.jdbcTemplate.queryForObject("SELECT COUNT(id) AS totalOrders FROM orders", Long.class);
        
        Long totalPlatforms = this.jdbcTemplate.queryForObject("SELECT COUNT(id) AS totalPlatforms FROM platform", Long.class);
        
        Double totalOrderMoney = this.jdbcTemplate.queryForObject("SELECT SUM(payment_amount) AS totalOrderMoney FROM orders", Double.class);

        if (totalOrderMoney == null)
            totalOrderMoney = 0.0; 

        List<AccountsByPlatformQuery> accountsByPlatform = this.jdbcTemplate.query("SELECT p.name AS name, COUNT(a.id) AS quantity FROM platform p LEFT JOIN account AS a ON a.platform_id = p.id GROUP BY p.id, p.name", new DataClassRowMapper<>(AccountsByPlatformQuery.class));

        List<MembershipNearToExpireQuery> membershipNearToExpireQueries = this.jdbcTemplate.query("SELECT a.email AS email, m.membership_expire_date AS membershipExpireDate, m.is_free_trial AS isFreeTrial, p.name AS platform FROM account a INNER JOIN membership AS m ON m.account = a.id INNER JOIN platform AS p ON p.id = a.platform_id WHERE m.membership_expire_date < CURRENT_DATE OR m.membership_expire_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL 5 DAY", new DataClassRowMapper<>(MembershipNearToExpireQuery.class));

        List<OrderDetailsSnapshotQuery> ordersNearToExpireQuery = this.jdbcTemplate.query(
            "SELECT * FROM order_details_snapshot WHERE finish_date < CURRENT_DATE OR finish_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL 5 DAY", 
            new DataClassRowMapper<>(OrderDetailsSnapshotQuery.class) 
        );

        return new DashboardInfoQuery(totalAccounts, totalOrders, totalPlatforms, totalOrderMoney, accountsByPlatform, membershipNearToExpireQueries, ordersNearToExpireQuery);
    }
    
}
