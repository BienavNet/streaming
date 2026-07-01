package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.MembershipConfigMapper;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.repositories.MembershipConfigJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MembershipConfigIRepository implements MembershipConfigInterface {

    private final MembershipConfigJpaRepository jpa;
    private final MembershipConfigMapper mapper;


    @Override
    public Optional<MembershipConfig> getMembershipConfig(MembershipConfigId id) {
        return this.jpa.findById(id.value())
            .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public List<MembershipConfig> getMembershipConfigsByPlatform(PlatformId platform) {
        List<MembershipConfigEntity> configs = this.jpa.findByPlatform(platform.value());
        return this.mapper.fromEntityListToDomainList(configs);
    }

    @Override
    public MembershipConfig saveMembershipConfig(MembershipConfig config) {
        MembershipConfigEntity savedEntity = this.jpa.saveAndFlush(this.mapper.fromDomainToEntity(config));
        return this.mapper.fromEntityToDomain(savedEntity);
    }

    @Override
    public void deleteMembershipConfig(MembershipConfigId id) {
        this.jpa.deleteById(id.value());
    }
    
}
