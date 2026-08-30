package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.projections.AccountDetailsProjection;

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
    Page<AccountDetailsProjection> findAllAccountDetails(Pageable pageable);

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
        WHERE a.email LIKE CONCAT('%', :criteria, '%');
        """,
        nativeQuery = true
    )
    List<AccountDetailsProjection> findByEmailContainingIgnoreCase(@Param("criteria") String criteria);


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
        WHERE p.name LIKE CONCAT('%', :criteria, '%');
        """,
        nativeQuery = true
    )
    List<AccountDetailsProjection> findByPlatformNameContainingIgnoreCase(@Param("criteria") String criteria);

    List<AccountEntity> findByPayment(String paymentId);
}
