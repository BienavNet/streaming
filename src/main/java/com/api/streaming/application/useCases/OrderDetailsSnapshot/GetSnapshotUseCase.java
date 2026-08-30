package com.api.streaming.application.useCases.OrderDetailsSnapshot;

import com.api.streaming.application.interfaces.OrderDetailsSnapshotInterface;
import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetSnapshotUseCase {
    
    private final OrderDetailsSnapshotInterface snapshotInterface;

    public OrderDetailsSnapshotQuery execute(String id) {
        return this.snapshotInterface.getSnapshot(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
