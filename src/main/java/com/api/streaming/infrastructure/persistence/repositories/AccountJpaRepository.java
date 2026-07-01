package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.streaming.infrastructure.persistence.dtos.AccountDetailsProjection;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByPlatform(String id);
    List<AccountEntity> findByEmailAndPlatform(String email, String platform);

    @Query(
        value = """
        SELECT
            a.id,
            a.email,
            a.password,
            a.is_free_trial,
            a.membership_buy_date,
            a.membership_expired_date,
            a.registered_at,
            a.status,
            p.id,
            p.name,
            p.link,
            p.icon,
            pa.id,
            pa.name,
            pa.month,
            pa.year,
            pa.cvv,
            pa.bank_name,
            m.id,
            mc.id,
            mc.plan,
            mc.profiles
        AS AccountDetails
        FROM account a
        LEFT JOIN platform AS p ON a.platform_id = p.id
        LEFT JOIN payment AS pa ON a.payment_id = pa.id
        LEFT JOIN membership AS m ON a.id = m.account
        LEFT JOIN membership_config AS mc ON m.membership_config = mc.id;
        """,
        nativeQuery = true
    )
    List<AccountDetailsProjection> findAllAccountDetails();

    @Query(
        value = """
        SELECT
            a.id,
            a.email,
            a.password,
            a.is_free_trial,
            a.membership_buy_date,
            a.membership_expired_date,
            a.registered_at,
            a.status,
            p.id,
            p.name,
            p.link,
            p.icon,
            pa.id,
            pa.name,
            pa.month,
            pa.year,
            pa.cvv,
            pa.bank_name,
            m.id,
            mc.id,
            mc.plan,
            mc.profiles
        AS AccountDetails
        FROM account a
        LEFT JOIN platform AS p ON a.platform_id = p.id
        LEFT JOIN payment AS pa ON a.payment_id = pa.id
        LEFT JOIN membership AS m ON a.id = m.account
        LEFT JOIN membership_config AS mc ON m.membership_config = mc.id
        WHERE a.id = :id;
        """,
        nativeQuery = true
    )
    Optional<AccountDetailsProjection> findAccountDetailsById(String id);
}
