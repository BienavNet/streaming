package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;

public interface MembershipConfigJpaRepository extends JpaRepository<MembershipConfigEntity, String> {
    List<MembershipConfigEntity> findByPlatform(String platform);
}
