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
            a.registered_at,
            p.id AS platformId,
            p.name AS platformName,
            p.link AS platformLink,
            p.icon AS platformIcon,
            pa.id AS paymentId,
            pa.number AS paymentNumber,
            pa.`month` AS paymentMonth,
            pa.`year` AS paymentYear,
            pa.cvv AS paymentCvv,
            pa.bank_name as paymentBankName,
            m.id AS membershipId,
            m.is_free_trial,
            m.membership_buy_date AS membershipBuyDate,
            m.membership_expire_date AS membershipExpireDate,
            mc.id AS membershipConfigId,
            mc.plan AS configPlan,
            mc.profiles AS configProfiles
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
            a.registered_at,
            p.id AS platformId,
            p.name AS platformName,
            p.link AS platformLink,
            p.icon AS platformIcon,
            pa.id AS paymentId,
            pa.number AS paymentNumber,
            pa.`month` AS paymentMonth,
            pa.`year` AS paymentYear,
            pa.cvv AS paymentCvv,
            pa.bank_name as paymentBankName,
            m.id AS membershipId,
            m.is_free_trial,
            m.membership_buy_date,
            m.membership_expire_date,
            mc.id AS membershipConfigId,
            mc.plan AS configPlan,
            mc.profiles AS configProfiles
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
