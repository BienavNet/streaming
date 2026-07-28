package com.api.streaming.infrastructure.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import java.util.List;


public interface MembershipJpaRepository extends JpaRepository<MembershipEntity, String> {
    Optional<MembershipEntity> findByAccount(String account);
    List<MembershipEntity> findByMembershipConfig(String membershipConfig);
}
