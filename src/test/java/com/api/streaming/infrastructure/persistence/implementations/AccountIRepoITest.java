package com.api.streaming.infrastructure.persistence.implementations;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.AccountMapperImpl;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;
import com.api.streaming.infrastructure.persistence.repositories.AccountJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipConfigJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.PaymentJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.PlatformJpaRepository;

@DataJpaTest
@Import({AccountIRepository.class, AccountMapperImpl.class, PaymentMapperImpl.class, PlatformMapperImpl.class})
public class AccountIRepoITest {
    
    @Autowired
    private AccountIRepository accountIRepository;

    @Autowired
    private AccountJpaRepository jpaRepository;

    @Autowired
    private PlatformJpaRepository platformJpaRepository;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private MembershipJpaRepository membershipJpaRepository;
    
    @Autowired
    private MembershipConfigJpaRepository membershipConfigJpaRepository;


    private AccountEntity createAccountEntity(String id, String email, String platformEntity, String paymentEntity) {
        AccountEntity entity = new AccountEntity();
        entity.setId(id);
        entity.setEmail(email);
        entity.setPassword("123456789");
        entity.setRegisteredAt(LocalDate.now());
        entity.setPlatform(platformEntity);
        entity.setPayment(paymentEntity);
        entity.setStatus("ACTIVE");

        return entity;
    }

    private PlatformEntity createAndSavePlatform() {
        PlatformEntity entity = new PlatformEntity();
        entity.setId("uuid-9824");
        entity.setName("netflix");
        entity.setLink("https://www.netflix.com");
        entity.setIcon("netflix-icon");
        return this.platformJpaRepository.saveAndFlush(entity);
    }

    private PaymentEntity createAndSavePayment() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("uuid-2234");
        entity.setNumber("4545545445545454");
        entity.setMonth(10);
        entity.setYear(2028);
        entity.setCvv("123");
        entity.setBankName("Banco");

        return this.paymentJpaRepository.saveAndFlush(entity);
    }

    private MembershipEntity createAndSaveMembership(String membershipId, String accountId, String membershipConfigId) {
        MembershipEntity entity = new MembershipEntity(membershipId, accountId, membershipConfigId, false, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        return this.membershipJpaRepository.saveAndFlush(entity);
    }

    private MembershipConfigEntity createAndSaveMembershipConfig(String platform) {
        return this.membershipConfigJpaRepository
                .saveAndFlush(new MembershipConfigEntity("uuid-23422", platform, "basic", 10));
    }

    @Test
    void shouldFindAccountById() {
        AccountId accountId = new AccountId("uuid-2344");
        PaymentEntity paymentEntity = this.createAndSavePayment();
        PlatformEntity platformEntity = this.createAndSavePlatform();
        AccountEntity accountEntity = this.createAccountEntity(accountId.value(), "test@gmail.com", platformEntity.getId(), paymentEntity.getId());

        this.jpaRepository.saveAndFlush(accountEntity);

        Optional<Account> result = this.accountIRepository.getAccount(accountId);
        assertThat(result).isPresent();

        assertThat(result.get().getId().value())
        .isEqualTo(accountEntity.getId());

        assertThat(result.get().getPlatform().value())
        .isEqualTo(accountEntity.getPlatform());

        assertThat(result.get().getPayment().value())
        .isEqualTo(accountEntity.getPayment());

        assertThat(result.get().getEmail().value())
        .isEqualTo(accountEntity.getEmail());
    }

    @Test
    void shouldFindAllAccounts() {
        AccountId accountId1 = new AccountId("uuid-2344");
        AccountId accountId2 = new AccountId("uuid-23421");
        PaymentEntity paymentEntity = this.createAndSavePayment();
        PlatformEntity platformEntity = this.createAndSavePlatform();
        AccountEntity accountEntity1 = this.createAccountEntity(accountId1.value(), "test@gmail.com", platformEntity.getId(), paymentEntity.getId());

        AccountEntity accountEntity2 = this.createAccountEntity(accountId2.value(), "tests@gmail.com", platformEntity.getId(), paymentEntity.getId());

        this.jpaRepository.saveAllAndFlush(List.of(accountEntity1, accountEntity2));

        List<Account> results = this.accountIRepository.getAccounts();

        assertThat(results)
        .isNotNull()
        .hasSize(2);
    }

    @Test
    void shouldFindAccountsByPlatform() {
        AccountId accountId1 = new AccountId("uuid-2344");
        AccountId accountId2 = new AccountId("uuid-23421");
        PaymentEntity paymentEntity = this.createAndSavePayment();
        PlatformEntity platformEntity = this.createAndSavePlatform();
        AccountEntity accountEntity1 = this.createAccountEntity(accountId1.value(), "test@gmail.com", platformEntity.getId(), paymentEntity.getId());

        AccountEntity accountEntity2 = this.createAccountEntity(accountId2.value(), "tests@gmail.com", platformEntity.getId(), paymentEntity.getId());

        this.jpaRepository.saveAllAndFlush(List.of(accountEntity1, accountEntity2));

        List<Account> results = this.accountIRepository.getAccountsByPlatform(new PlatformId(platformEntity.getId()));

        assertThat(results)
        .isNotNull()
        .hasSize(2);
    }

    @Test
    void shouldCreateAndSaveANewAccount() {
        AccountId accountId = new AccountId("uuid-2344");
        PaymentEntity paymentEntity = this.createAndSavePayment();
        PlatformEntity platformEntity = this.createAndSavePlatform();
        // AccountEntity accountEntity = this.createAccountEntity(accountId.value(), "test@gmail.com", platformEntity, paymentEntity);

        Account accountToSave = new Account(accountId, new Email("test@gmail.com"), "12423524", LocalDate.now(), new PlatformId(platformEntity.getId()), new PaymentId(paymentEntity.getId()), AccountStatus.ACTIVE);


        Account savedAccount = this.accountIRepository.saveAccount(accountToSave);

        Optional<AccountEntity> savedInDB = this.jpaRepository.findById(accountId.value());

        assertThat(savedInDB).isPresent();

        assertThat(savedAccount)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(accountToSave);
    }

    @Test
    void shouldDeleteAccountById() {
        AccountId accountId = new AccountId("uuid-2344");
        PaymentEntity paymentEntity = this.createAndSavePayment();
        PlatformEntity platformEntity = this.createAndSavePlatform();
        AccountEntity accountEntity = this.createAccountEntity(accountId.value(), "test@gmail.com", platformEntity.getId(), paymentEntity.getId());

        this.jpaRepository.saveAndFlush(accountEntity);

        this.accountIRepository.deleteAccount(accountId);

        Optional<AccountEntity> result = this.jpaRepository.findById(accountId.value());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindAllAccountDetails() {
        AccountId accountId1 = new AccountId("uuid-2344");
        AccountId accountId2 = new AccountId("uuid-23421");
        PaymentEntity paymentEntity = this.createAndSavePayment();
        PlatformEntity platformEntity = this.createAndSavePlatform();
        AccountEntity accountEntity1 = this.createAccountEntity(accountId1.value(), "test@gmail.com", platformEntity.getId(), paymentEntity.getId());

        AccountEntity accountEntity2 = this.createAccountEntity(accountId2.value(), "tests@gmail.com", platformEntity.getId(), paymentEntity.getId());

        MembershipConfigEntity membershipConfigEntity = this.createAndSaveMembershipConfig(platformEntity.getId());
        this.createAndSaveMembership("uuid-12983", accountId1.value(), membershipConfigEntity.getId());
        this.createAndSaveMembership("uuid-129823", accountId2.value(), membershipConfigEntity.getId());

        this.jpaRepository.saveAllAndFlush(List.of(accountEntity1, accountEntity2));

        List<AccountDetailsQuery> results = this.accountIRepository.getAccountsDetails();

        assertThat(results)
        .isNotNull()
        .hasSize(2);
    }

    @Test
    void shouldFindAccountDetailsById() {
        AccountId accountId = new AccountId("uuid-2344");
        PaymentEntity paymentEntity = this.createAndSavePayment();
        PlatformEntity platformEntity = this.createAndSavePlatform();
        AccountEntity accountEntity = this.createAccountEntity(accountId.value(), "test@gmail.com", platformEntity.getId(), paymentEntity.getId());
        MembershipConfigEntity membershipConfigEntity = this.createAndSaveMembershipConfig(platformEntity.getId());
        this.createAndSaveMembership("uuid-12983", accountId.value(), membershipConfigEntity.getId());

        this.jpaRepository.saveAndFlush(accountEntity);

        Optional<AccountDetailsQuery> result = this.accountIRepository.getAccountDetailById(accountId);
        assertThat(result).isPresent();

        System.out.println(result);

        assertThat(result.get().id().value())
        .isEqualTo(accountEntity.getId());

        assertThat(result.get().payment().getId().value())
        .isEqualTo(accountEntity.getPayment());

        assertThat(result.get().platform().getId().value())
        .isEqualTo(accountEntity.getPlatform());

    }

 
}
