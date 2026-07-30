package com.api.streaming.infrastructure.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import com.api.streaming.infrastructure.persistence.projections.MembershipDetailsProjection;

import java.util.List;


public interface MembershipJpaRepository extends JpaRepository<MembershipEntity, String> {
    Optional<MembershipEntity> findByAccount(String account);
    List<MembershipEntity> findByMembershipConfig(String membershipConfig);

    @Query(
        value = """
                SELECT 
                m.id,
                m.isFree_trial as isFreeTrial,
                m.membership_buy_date as membershipBuyDate,
                m.membership_expire_date as membershipExpireDate,
                c.id,
                c.platform_id as platformId,
                c.plan,
                c.profiles
                FROM 
                membership m
                LEFT JOIN membership_config AS c ON m.membership_config = c.id
                WHERE m.membership_config = :id
                """,
        nativeQuery = true
    )
    List<MembershipDetailsProjection> findAllMembershipDetailsByMembershipConfigId(String id);

    @Query(
        value = """
                SELECT 
                m.id,
                m.isFree_trial as isFreeTrial,
                m.membership_buy_date as membershipBuyDate,
                m.membership_expire_date as membershipExpireDate,
                c.id,
                c.platform_id as platformId,
                c.plan,
                c.profiles
                FROM 
                membership m
                LEFT JOIN membership_config AS c ON m.membership_config = c.id
                WHERE m.id = :id
                """,
        nativeQuery = true
    )
    Optional<MembershipDetailsProjection> findMembershipDetailsById(String id);

    @Query(
        value = """
                SELECT 
                m.id,
                m.isFree_trial as isFreeTrial,
                m.membership_buy_date as membershipBuyDate,
                m.membership_expire_date as membershipExpireDate,
                c.id,
                c.platform_id as platformId,
                c.plan,
                c.profiles
                FROM 
                membership m
                LEFT JOIN membership_config AS c ON m.membership_config = c.id
                WHERE m.account = :id
                """,
        nativeQuery = true
    )
    Optional<MembershipDetailsProjection> findMembershipDetailsByAccountId(String id);

    @Query(
        value = """
                SELECT 
                m.id,
                m.isFree_trial as isFreeTrial,
                m.membership_buy_date as membershipBuyDate,
                m.membership_expire_date as membershipExpireDate,
                c.id,
                c.platform_id as platformId,
                c.plan,
                c.profiles
                FROM 
                membership m
                LEFT JOIN membership_config AS c ON m.membership_config = c.id
                """,
        nativeQuery = true
    )
    Optional<MembershipDetailsProjection> findAllMembershipDetails();
}
