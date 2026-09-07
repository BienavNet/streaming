package com.api.streaming.application.useCases.Account;

import java.util.List;
import java.util.Map;

import com.api.streaming.application.interfaces.strategies.AccountFilterStrategy;
import com.api.streaming.application.queries.AccountDetailsQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilterAccountUseCase {
    private final Map<String, AccountFilterStrategy> filterStrategies;

    public List<AccountDetailsQuery> execute(String filterKey, String criteria ) {
        AccountFilterStrategy filter = this.filterStrategies.get(filterKey);

        if (filter == null)
            throw new IllegalArgumentException("This filter key does not exist.");

        return filter.filter(criteria);
    }
}
