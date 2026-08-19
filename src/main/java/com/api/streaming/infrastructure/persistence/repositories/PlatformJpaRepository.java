package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;

public interface PlatformJpaRepository extends JpaRepository<PlatformEntity, String> {

    Optional<PlatformEntity> findByName(String name);
    
    List<PlatformEntity> findByNameContainingIgnoreCase(String name);
}
