package com.api.streaming.application.interfaces;

import java.util.List;

import com.api.streaming.application.queries.AccountDetailsQuery;

public interface AccountFilterStrategy {
    List<AccountDetailsQuery> filter(String criteria);
}
