package com.api.streaming.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.streaming.infrastructure.persistence.entities.CronTask;

public interface CronTaskJpaRepository extends JpaRepository<CronTask, String> {
    
}
