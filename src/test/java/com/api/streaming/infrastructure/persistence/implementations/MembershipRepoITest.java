package com.api.streaming.infrastructure.persistence.implementations;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;
import com.api.streaming.infrastructure.mappers.AccountMapperImpl;
import com.api.streaming.infrastructure.mappers.MembershipConfigMapperImpl;
import com.api.streaming.infrastructure.mappers.MembershipMapperImpl;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import com.api.streaming.infrastructure.persistence.repositories.MembershipConfigJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipJpaRepository;

@DataJpaTest
@Import(
    {
        MembershipIRepository.class, 
        MembershipMapperImpl.class, 
        AccountMapperImpl.class,
        PlatformMapperImpl.class,
        PaymentMapperImpl.class,
        MembershipConfigMapperImpl.class
    }
)
public class MembershipRepoITest {

    @Autowired
    private MembershipIRepository membershipIRepository;

    @Autowired
    private MembershipJpaRepository jpaRepository;

    @Autowired
    private MembershipConfigJpaRepository configJpaRepository;

    private MembershipEntity createMembershipEntity(String id, String account, String configId) {
        MembershipEntity entity = new MembershipEntity();
        entity.setId(id);
        entity.setAccount(account);
        entity.setMembershipConfig(configId);
        entity.setFreeTrial(false);
        entity.setMembershipBuyDate(LocalDate.now());
        entity.setMembershipExpireDate(LocalDate.now().plusMonths(1));
        return entity;
    }

    private MembershipConfigEntity createConfigEntity(String id) {
        MembershipConfigEntity entity = new MembershipConfigEntity();
        entity.setId(id);
        entity.setPlatform("plat-11");
        entity.setPlan("PREMIUM");
        entity.setProfiles(4);
        return entity;
    }

    @Test
    void shouldFindMembershipById() {
        MembershipEntity entity = this.createMembershipEntity("mem-1", "acc-100", "cfg-1");
        this.jpaRepository.saveAndFlush(entity);

        Optional<Membership> result = this.membershipIRepository.getMembership(new MembershipId(entity.getId()));

        assertThat(result).isPresent();
        assertThat(result.get())
            .usingRecursiveComparison()
            .ignoringFields("id", "account", "membershipConfig")
            .isEqualTo(entity);
    }

    @Test
    void shouldFindMembershipByAccount() {
        MembershipEntity entity = this.createMembershipEntity("mem-1", "acc-200", "cfg-1");
        this.jpaRepository.saveAndFlush(entity);

        Optional<Membership> result = this.membershipIRepository.getMembershipByAccount(new AccountId(entity.getAccount()));

        assertThat(result).isPresent();
        assertThat(result.get().getId().value()).isEqualTo(entity.getId());
    }

    @Test
    void shouldReturnAllMemberships() {
        MembershipEntity entity1 = this.createMembershipEntity("mem-1", "acc-1", "cfg-1");
        MembershipEntity entity2 = this.createMembershipEntity("mem-2", "acc-2", "cfg-1");
        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2));

        List<Membership> result = this.membershipIRepository.getMemberships();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldSaveNewMembership() {

        Membership membershipToSave = new Membership(new MembershipId("mem-99"), new AccountId("acc-99"), new MembershipConfigId("conf-99"), false, LocalDate.now(), LocalDate.now().plusMonths(1));

        Membership savedMembership = this.membershipIRepository.saveMembership(membershipToSave);
        Optional<MembershipEntity> savedInDB = this.jpaRepository.findById(membershipToSave.getId().value());

        assertThat(savedInDB).isPresent();
        assertThat(savedMembership)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id", "account", "membershipConfig")
            .isEqualTo(savedInDB.get());
    }

    @Test
    void shouldDeleteARegisteredMembershipById() {
        MembershipEntity entity = this.createMembershipEntity("mem-1", "acc-1", "cfg-1");
        this.jpaRepository.saveAndFlush(entity);

        this.membershipIRepository.deleteMembership(new MembershipId(entity.getId()));
        Optional<MembershipEntity> deleted = this.jpaRepository.findById(entity.getId());

        assertThat(deleted).isEmpty();
    }

    @Test
    void shouldReturnAllMembershipDetails() {
        MembershipConfigEntity config = this.createConfigEntity("cfg-1");
        this.configJpaRepository.saveAndFlush(config);

        MembershipEntity entity1 = this.createMembershipEntity("mem-1", "acc-1", "cfg-1");
        MembershipEntity entity2 = this.createMembershipEntity("mem-2", "acc-2", "cfg-1");
        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2));

        Pageable pageable = new Pageable(0, 2);
        Page<MembershipDetailsQuery> result = this.membershipIRepository.getAllMembershipDetails(pageable);

        assertThat(result.content()).hasSize(2);
        assertThat(result.content().get(0).details()).isNotNull();
        assertThat(result.content().get(0).details().getPlan()).isEqualTo("PREMIUM");
    }

    @Test
    void shouldGetMembershipDetailsByAccount() {
        MembershipConfigEntity config = this.createConfigEntity("cfg-1");
        this.configJpaRepository.saveAndFlush(config);

        MembershipEntity entity = this.createMembershipEntity("mem-1", "acc-777", "cfg-1");
        this.jpaRepository.saveAndFlush(entity);

        Optional<MembershipDetailsQuery> result = this.membershipIRepository.getMembershipDetailsByAccount(new AccountId("acc-777"));

        assertThat(result).isPresent();
        assertThat(result.get().id().value()).isEqualTo("mem-1");
        assertThat(result.get().details().getPlan()).isEqualTo("PREMIUM");
    }

    @Test
    void shouldGetMembershipDetailsById() {
        MembershipConfigEntity config = this.createConfigEntity("cfg-1");
        this.configJpaRepository.saveAndFlush(config);

        MembershipEntity entity = this.createMembershipEntity("mem-55", "acc-1", "cfg-1");
        this.jpaRepository.saveAndFlush(entity);

        Optional<MembershipDetailsQuery> result = this.membershipIRepository.getMembershipDetailsById(new MembershipId("mem-55"));

        assertThat(result).isPresent();
        assertThat(result.get().details()).isNotNull();
    }
}  

