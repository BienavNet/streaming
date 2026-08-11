package com.api.streaming.application.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

public interface OrderDetailsInterface {
    List<OrderDetailsQuery> getOrdersDetailsByAccountId(AccountId accountId);
    Optional<OrderDetailsQuery> getOrderDetailsById(OrderId orderId);
    Page<OrderDetailsQuery> getOrdersDetails(Pageable pageable);
}
