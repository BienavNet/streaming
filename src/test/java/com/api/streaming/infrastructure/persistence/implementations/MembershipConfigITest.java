package com.api.streaming.infrastructure.persistence.implementations;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.MembershipConfigMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.repositories.MembershipConfigJpaRepository;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@Import({MembershipConfigIRepository.class, MembershipConfigMapperImpl.class, PlatformMapperImpl.class})
public class MembershipConfigITest {

    @Autowired
    private MembershipConfigIRepository membershipConfigIRepository;

    @Autowired
    private MembershipConfigJpaRepository jpaRepository;

    private MembershipConfigEntity createMembershipConfigEntity(String id, String platform, String plan) {
        MembershipConfigEntity entity = new MembershipConfigEntity();
        entity.setId(id);
        entity.setPlatform(platform);
        entity.setPlan(plan);
        entity.setProfiles(4);
        return entity;
    }

    @Test
    void shouldFindMembershipConfigById() {
        MembershipConfigEntity entity = this.createMembershipConfigEntity("config-1", "NETFLIX", "PREMIUM");
        this.jpaRepository.saveAndFlush(entity);

        MembershipConfigId id = new MembershipConfigId(entity.getId());
        Optional<MembershipConfig> result = this.membershipConfigIRepository.getMembershipConfig(id);

        assertThat(result).isPresent();

        assertThat(result.get())
            .usingRecursiveComparison()
            .ignoringFields("id", "platform")
            .isEqualTo(entity);
    }

    @Test
    void shouldReturnMembershipConfigsByPlatform() {
        MembershipConfigEntity entity1 = this.createMembershipConfigEntity("config-1", "NETFLIX", "BASIC");
        MembershipConfigEntity entity2 = this.createMembershipConfigEntity("config-2", "NETFLIX", "PREMIUM");
        MembershipConfigEntity entity3 = this.createMembershipConfigEntity("config-3", "DISNEY", "STANDARD");

        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2, entity3));

        PlatformId platformId = new PlatformId("NETFLIX");
        List<MembershipConfig> result = this.membershipConfigIRepository.getMembershipConfigsByPlatform(platformId);

        assertThat(result)
            .isNotNull()
            .hasSize(2);
    }

    @Test
    void shouldSaveNewMembershipConfig() {
        MembershipConfig configToSave = new MembershipConfig(
            new MembershipConfigId("config-99"),
            new PlatformId("AMAZON"),
            "STANDARD",
            2
        );

        MembershipConfig savedConfig = this.membershipConfigIRepository.saveMembershipConfig(configToSave);

        Optional<MembershipConfigEntity> savedInDB = this.jpaRepository.findById(configToSave.getId().value());

        assertThat(savedInDB).isPresent();

        assertThat(savedConfig)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id", "platform")
            .isEqualTo(savedInDB.get());
    }

    @Test
    void shouldDeleteARegisteredMembershipConfigById() {
        MembershipConfigEntity entity = this.createMembershipConfigEntity("config-1", "HBO", "STANDARD");
        this.jpaRepository.saveAndFlush(entity);

        this.membershipConfigIRepository.deleteMembershipConfig(new MembershipConfigId(entity.getId()));

        Optional<MembershipConfigEntity> deletedConfig = this.jpaRepository.findById(entity.getId());

        assertThat(deletedConfig).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldsAreNull() {
        // Al tener @NotNull en la entidad, se fuerza el error de integridad al enviar campos nulos
        MembershipConfigEntity entity = this.createMembershipConfigEntity("config-fail", null, "PREMIUM");

        assertThatThrownBy(() -> this.jpaRepository.saveAndFlush(entity))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldReturnEmptyOptionalWhenMembershipConfigDoesNotExist() {
        MembershipConfigId id = new MembershipConfigId("non-existent-id");

        assertThat(this.membershipConfigIRepository.getMembershipConfig(id))
            .isEmpty();
    }
}
