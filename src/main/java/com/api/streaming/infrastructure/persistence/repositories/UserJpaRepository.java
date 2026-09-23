package com.api.streaming.infrastructure.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.UserEntity;


public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
}
