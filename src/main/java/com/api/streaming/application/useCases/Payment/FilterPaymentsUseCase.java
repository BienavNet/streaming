package com.api.streaming.application.useCases.Payment;

import java.util.List;
import java.util.Map;

import com.api.streaming.application.interfaces.strategies.PaymentFilterStrategy;
import com.api.streaming.domain.models.Payment;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilterPaymentsUseCase {
    private final Map<String, PaymentFilterStrategy> filterStrategies;

    public List<Payment> execute(String filterKey, String criteria) {
        PaymentFilterStrategy filter = this.filterStrategies.get(filterKey);

        if (filter == null)
            throw new IllegalArgumentException("This filter key does not exist.");

        return filter.execute(criteria);
    }
}
