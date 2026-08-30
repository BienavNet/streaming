package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.OrderDetailsSnapshotInterface;
import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;
import com.api.streaming.infrastructure.mappers.OrderDetailsSnapshotMapper;
import com.api.streaming.infrastructure.persistence.entities.OrderDetailsSnapshot;
import com.api.streaming.infrastructure.persistence.repositories.OrderDetailsSnapshotJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderDetailsSnapshotIRepository implements OrderDetailsSnapshotInterface {
    private final OrderDetailsSnapshotJpaRepository jpa;
    private final OrderDetailsSnapshotMapper mapper;

    public void saveSnapshot(OrderDetailsSnapshot snapshot) {
        this.jpa.saveAndFlush(snapshot);
    }

    @Override
    public Optional<OrderDetailsSnapshotQuery> getSnapshot(String id) {
        return this.jpa.findById(id)
                .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public com.api.streaming.domain.valueObjects.Page<OrderDetailsSnapshotQuery> getSnapshots(
            com.api.streaming.domain.valueObjects.Pageable pageable) {
        Pageable springPageable = PageRequest.of(pageable.getPage(), pageable.getOffset());
        Page<OrderDetailsSnapshot> springPage = this.jpa.findAll(springPageable);

        List<OrderDetailsSnapshotQuery> content = springPage.getContent().stream().map(this.mapper::fromEntityToDomain).toList();

        return new com.api.streaming.domain.valueObjects.Page<>(content, springPage.getTotalElements(), springPage.getTotalPages());
    }

    @Override
    public void saveSnapshot(OrderDetailsSnapshotQuery query) {
        this.jpa.saveAndFlush(this.mapper.fromDomainToEntity(query));
    }
}
