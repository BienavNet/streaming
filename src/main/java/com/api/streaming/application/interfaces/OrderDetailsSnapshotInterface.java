package com.api.streaming.application.interfaces;

import java.util.Optional;

import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

public interface OrderDetailsSnapshotInterface {
    Optional<OrderDetailsSnapshotQuery> getSnapshot(String id);
    Page<OrderDetailsSnapshotQuery> getSnapshots(Pageable pageable);
    void saveSnapshot(OrderDetailsSnapshotQuery query);
}
