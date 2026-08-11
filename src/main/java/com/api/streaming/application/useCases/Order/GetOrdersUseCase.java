package com.api.streaming.application.useCases.Order;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetOrdersUseCase {
    private final OrderDetailsInterface orderDetailsInterface;

    public Page<OrderDetailsQuery> execute(Pageable pageable) {

        if (pageable == null)
            pageable = new Pageable(0, 10);

        Page<OrderDetailsQuery> page = this.orderDetailsInterface.getOrdersDetails(pageable);
        return page;
    }
}
