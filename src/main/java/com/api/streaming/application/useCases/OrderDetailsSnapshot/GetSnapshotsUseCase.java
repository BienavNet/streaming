package com.api.streaming.application.useCases.OrderDetailsSnapshot;

import com.api.streaming.application.interfaces.OrderDetailsSnapshotInterface;
import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetSnapshotsUseCase {
    private final OrderDetailsSnapshotInterface snapshotInterface;

    public Page<OrderDetailsSnapshotQuery> execute(Pageable pageable) {

        if (pageable == null)
            pageable = new Pageable(0, 10);

        return this.snapshotInterface.getSnapshots(pageable);
    }
}
