package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.queries.MembershipDetailsQuery;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.mappers.MembershipMapper;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import com.api.streaming.infrastructure.persistence.repositories.MembershipJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MembershipIRepository implements MembershipInterface, MembershipDetailsInterface {

    private final MembershipJpaRepository jpa;
    private final MembershipMapper mapper;

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

        return this.jpa.findAllMembershipDetails()
                .stream()
                .map(entity -> {
                    return this.mapper.fromProjectionToDetailsQuery(entity);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MembershipDetailsQuery> getMembershipDetailsByAccount(AccountId accountId) {

        return this.jpa.findMembershipDetailsByAccountId(accountId.value())
                .map(entity -> {
                    return this.mapper.fromProjectionToDetailsQuery(entity);
                });
    }

    @Override
    public Optional<MembershipDetailsQuery> getMembershipDetailsById(MembershipId id) {

        return this.jpa.findMembershipDetailsById(id.value())
                .map(entity -> {
                    return this.mapper.fromProjectionToDetailsQuery(entity);
                });


    }

    @Override
    public List<MembershipDetailsQuery> getMembershipsByMembershipConfig(MembershipConfigId id) {

        return this.jpa.findAllMembershipDetailsByMembershipConfigId(id.value())
                .stream()
                .map(entity -> {
                    return this.mapper.fromProjectionToDetailsQuery(entity);
                })
                .collect(Collectors.toList());
    }

}
