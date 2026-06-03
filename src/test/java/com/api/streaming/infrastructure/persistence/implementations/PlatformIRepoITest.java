package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;
import com.api.streaming.infrastructure.persistence.repositories.PlatformJpaRepository;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Import({PlatformIRepository.class, PlatformMapperImpl.class })
public class PlatformIRepoITest {
    
    @Autowired
    private PlatformIRepository platformIRepository;

    @Autowired
    private PlatformJpaRepository jpaRepository;

    private PlatformEntity setup(String id, String name){

        PlatformEntity entity = new PlatformEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setLink("https://www."+name+".com");
        entity.setIcon(""+name+"-icon");
        return entity;
    }

    @Test
    void shouldFindPlatformById() {

        PlatformEntity entity = setup("uuid-1232", "Netflix");
        this.jpaRepository.saveAndFlush(entity);

        Optional<Platform> result = this.platformIRepository.getPlatform(new PlatformId(entity.getId()));

        assertThat(result)
        .isNotNull()
        .isPresent();

        assertThat(result.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(entity);
    }

    @Test
    void shouldFindPlatformByName() {
        
        PlatformEntity entity = setup("uuid-7172", "Prime Video");
        this.jpaRepository.saveAndFlush(entity);

        Optional<Platform> result = this.platformIRepository.getByName("Prime Video");

        assertThat(result)
        .isNotNull()
        .isPresent();

        assertThat(result.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(entity);

    }

    @Test
    void shouldReturnAllPlatforms() {
        PlatformEntity entity1 = setup("uuid-7172", "Prime Video");
        PlatformEntity entity2 = setup("uuid-7174", "Netflix");

        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2));

        List<Platform> result = this.platformIRepository.getAllPlatforms();

        assertThat(result)
        .isNotEmpty()
        .hasSize(2);
    }

    @Test
    void shouldSaveNewPlatform() {
        PlatformId platformId = new PlatformId("uuid-283342");
        Platform platformToSave = new Platform(platformId, "Netflix", "https://www.netflix.com", "netflix-icon");

        Platform savedPlatform = this.platformIRepository.savePlatform(platformToSave);

        assertThat(savedPlatform).isNotNull();

        Optional<PlatformEntity> savedInDB = this.jpaRepository.findById(platformId.value());

        assertThat(savedInDB).isPresent();

        assertThat(savedPlatform)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(savedInDB.get());
    }

    @Test
    void shouldDeletePlatformById() {
        PlatformEntity entity = setup("uuid-7172", "Prime Video");
        this.jpaRepository.saveAndFlush(entity);

        PlatformId platformId = new PlatformId(entity.getId());

        this.platformIRepository.deletePlatform(platformId);

        Optional<PlatformEntity> deletedEntity = this.jpaRepository.findById(platformId.value());

        assertThat(deletedEntity).isEmpty();

    }

    @Test
    void shouldThrowExceptionWhenPlatformNameAlreadyExists() {
        PlatformEntity existingEntity = new PlatformEntity();
        existingEntity.setId("plat-111");
        existingEntity.setName("Netflix");
        existingEntity.setLink("https://netflix.com");
        existingEntity.setIcon("icon");
        this.jpaRepository.saveAndFlush(existingEntity);

        Platform duplicatePlatform = new Platform(
            new PlatformId("plat-222"), 
            "Netflix", 
            "https://netflix.com", 
            "icon"
        );

        assertThatThrownBy(() -> {
            this.platformIRepository.savePlatform(duplicatePlatform);
        }).isInstanceOf(DataIntegrityViolationException.class); 
    }

    @Test
    void shouldReturnEmptyOptionalWhenPlatformDoesNotExist() {
        // 1. ARRANGE
        PlatformId nonExistingId = new PlatformId("id-fantasma");

        // 2. ACT
        Optional<Platform> result = platformIRepository.getPlatform(nonExistingId);

        // 3. ASSERT
        assertThat(result).isEmpty();
    }
}
