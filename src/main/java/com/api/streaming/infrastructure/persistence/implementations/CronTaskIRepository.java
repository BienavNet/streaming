package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.CronTaskInterface;
import com.api.streaming.application.queries.CronTaskQuery;
import com.api.streaming.infrastructure.mappers.CronTaskMapper;
import com.api.streaming.infrastructure.persistence.repositories.CronTaskJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class CronTaskIRepository implements CronTaskInterface {

    private final CronTaskJpaRepository jpa;
    private final CronTaskMapper mapper;

    @Override
    public List<CronTaskQuery> getTasks() {
        return this.jpa.findAll()
                .stream()
                .map(this.mapper::fromEntityToQuery)
                .toList();
    }

    @Override
    public Optional<CronTaskQuery> getTask(String id) {
        return this.jpa.findById(id).map(this.mapper::fromEntityToQuery);
    }

    @Override
    public void save(CronTaskQuery query) {
        this.jpa.saveAndFlush(this.mapper.fromQueryToEntity(query));
    }
    
}
