package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.streaming.infrastructure.persistence.entities.OrderEntity;
import com.api.streaming.infrastructure.persistence.projections.OrderDetailsProjection;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByAccount(String account);
    List<OrderEntity> findByAccountAndOrderType(String id, String orderType);


    @Query(
        value = """
                SELECT
                a.email as accountEmail,
                p.name AS platformName,
                p.icon AS platformIcon,
                c.plan,
                o.id,
                o.account,
                o.profile_amount as profileAmount,
                o.buy_date as buyDate,
                o.finish_date as finishDate,
                o.payment_amount as paymentAmount,
                o.order_type as orderType
                FROM orders o
                LEFT JOIN account AS a ON o.account = a.id
                LEFT JOIN platform AS p ON a.platform_id = p.id
                LEFT JOIN membership_config AS c ON c.platform = p.id
                WHERE o.account = :id
                """,
        nativeQuery = true
    )
    List<OrderDetailsProjection> findOrderDetailsByAccountId(String id);

    @Query(
        value = """
                SELECT
                a.email as accountEmail,
                p.name AS platformName,
                p.icon AS platformIcon,
                c.plan,
                o.id,
                o.account,
                o.profile_amount as profileAmount,
                o.buy_date as buyDate,
                o.finish_date as finishDate,
                o.payment_amount as paymentAmount,
                o.order_type as orderType
                FROM orders o
                LEFT JOIN account AS a ON o.account = a.id
                LEFT JOIN platform AS p ON a.platform_id = p.id
                LEFT JOIN membership_config AS c ON c.platform = p.id
                WHERE o.id = :id
                """,
        nativeQuery = true
    )
    Optional<OrderDetailsProjection> findOrderDetailsById(String id);

    @Query(
        value = """
                SELECT
                a.email as accountEmail,
                p.name AS platformName,
                p.icon AS platformIcon,
                c.plan,
                o.id,
                o.account,
                o.profile_amount as profileAmount,
                o.buy_date as buyDate,
                o.finish_date as finishDate,
                o.payment_amount as paymentAmount,
                o.order_type as orderType
                FROM orders o
                LEFT JOIN account AS a ON o.account = a.id
                LEFT JOIN platform AS p ON a.platform_id = p.id
                LEFT JOIN membership_config AS c ON c.platform = p.id
                """,
        nativeQuery = true
    )
    List<OrderDetailsProjection> findAllOrderDetails();
}
