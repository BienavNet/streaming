package com.api.streaming.infrastructure.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByPlatform(String id);
    List<AccountEntity> findByEmailAndPlatform(String email, String platform);
}
