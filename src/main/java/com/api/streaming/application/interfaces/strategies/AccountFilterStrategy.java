package com.api.streaming.application.interfaces.strategies;

import java.util.List;

import com.api.streaming.application.queries.AccountDetailsQuery;

public interface AccountFilterStrategy {
    List<AccountDetailsQuery> filter(String criteria);
}
