package com.api.streaming.application.useCases.OrderDetailsSnapshot;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.interfaces.OrderDetailsSnapshotInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.application.queries.OrderDetailsSnapshotQuery;
import com.api.streaming.domain.valueObjects.OrderId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaveOrderSnapshotUseCase {

    private final OrderDetailsSnapshotInterface snapshotInterface;

    //external services
    private final OrderDetailsInterface orderDetailsInterface;

    public void execute(OrderId order) {

        OrderDetailsQuery query = this.orderDetailsInterface.getOrderDetailsById(order)
            .orElseThrow(() -> new EntityNotFoundException());

        OrderDetailsSnapshotQuery snapshot = new OrderDetailsSnapshotQuery(query.orderDetails().getId().value(), query.accountEmail().value(), query.platformName(), query.plan(), query.orderDetails().getProfileAmount(), query.orderDetails().getBuyDate(), query.orderDetails().getFinishDate(), query.orderDetails().getPaymentAmount(), query.orderDetails().getOrderType().getValue());

        this.snapshotInterface.saveSnapshot(snapshot);
    }
    
}
