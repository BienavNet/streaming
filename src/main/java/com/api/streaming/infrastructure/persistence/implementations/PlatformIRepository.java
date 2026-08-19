package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Platform;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.PlatformMapper;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;
import com.api.streaming.infrastructure.persistence.repositories.PlatformJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlatformIRepository implements PlatformInterface {

    private final PlatformJpaRepository jpa;
    private final PlatformMapper mapper;

    @Override
    public Optional<Platform> getPlatform(PlatformId platformId) {
        return this.jpa.findById(platformId.value())
            .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public Optional<Platform> getByName(String name) {
        return this.jpa.findByName(name)
            .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public List<Platform> getAllPlatforms() {
        List<PlatformEntity> entities = this.jpa.findAll();
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public Platform savePlatform(Platform platform) {
        PlatformEntity toSaveEntity = this.mapper.fromDomainToEntity(platform);
        PlatformEntity savedEntity = this.jpa.saveAndFlush(toSaveEntity);
        return this.mapper.fromEntityToDomain(savedEntity);
    }

    @Override
    public void deletePlatform(PlatformId platformId) {
        this.jpa.deleteById(platformId.value());
    }

    @Override
    public List<Platform> findByName(String name) {
        return this.jpa.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this.mapper::fromEntityToDomain)
                .toList();
    }
    
}
