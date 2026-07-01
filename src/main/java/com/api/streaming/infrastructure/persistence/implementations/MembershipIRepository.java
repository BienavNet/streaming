package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.mappers.MembershipMapper;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import com.api.streaming.infrastructure.persistence.repositories.MembershipConfigJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MembershipIRepository implements MembershipInterface, MembershipDetailsInterface {

    private final MembershipJpaRepository jpa;
    private final MembershipMapper mapper;

    // External repositories
    private final MembershipConfigJpaRepository membershipConfigjpa;

    @Override
    public Optional<Membership> getMembership(MembershipId id) {
        return this.jpa.findById(id.value())
                .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public Optional<Membership> getMembershipByAccount(AccountId id) {
        return this.jpa.findByAccount(id.value())
                .map(this.mapper::fromEntityToDomain);
                
    }

    @Override
    public List<Membership> getMemberships() {
        List<MembershipEntity> list = this.jpa.findAll();
        return this.mapper.fromEntityListToDomainList(list);
    }

    // @Override
    // public List<Membership> getMembershipsByPlatform(PlatformId platformId) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getMembershipsByPlatform'");
    // }

    @Override
    public Membership saveMembership(Membership membership) {
        MembershipEntity savedEntity = this.jpa.save(this.mapper.fromDomainToEntity(membership));
        return this.mapper.fromEntityToDomain(savedEntity);
    }

    @Override
    public void deleteMembership(MembershipId id) {
        this.jpa.deleteById(id.value());
    }

    @Override
    public List<MembershipDetailsQuery> getAllMembershipDetails() {
        List<MembershipEntity> membershipList = this.jpa.findAll();
        // List<MembershipConfigEntity> configList = this.membershipConfigjpa.findAll();

        Map<String, MembershipConfigEntity> config = this.membershipConfigjpa.findAll()
            .stream()
            .collect(Collectors.toMap(MembershipConfigEntity::getId, Function.identity()));

        return membershipList
            .stream()
            .map(membership -> {
                return this.mapper.fromEntityToQuery(membership.getId(), config.get(membership.getMembershipConfig()));
            })
            .collect(Collectors.toList());
    }

    @Override
    public Optional<MembershipDetailsQuery> getMembershipDetailsByAccount(AccountId accountId) {
        return this.jpa.findByAccount(accountId.value())
            .map(entity -> {
                Optional<MembershipConfigEntity> configEntity = this.membershipConfigjpa.findById(entity.getMembershipConfig());

                return this.mapper.fromEntityToQuery(entity.getId(), configEntity.get());
            });
    }

    @Override
    public Optional<MembershipDetailsQuery> getMembershipDetailsById(MembershipId id) {
        return this.jpa.findById(id.value())
            .map(entity -> {
                Optional<MembershipConfigEntity> configEntity = this.membershipConfigjpa.findById(entity.getMembershipConfig());

                return this.mapper.fromEntityToQuery(entity.getId(), configEntity.get());
            });
    }

    
    
}
