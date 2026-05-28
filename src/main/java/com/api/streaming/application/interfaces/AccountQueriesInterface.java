package com.api.streaming.application.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;

public interface AccountQueriesInterface {
    List<AccountDetailsQuery> getAccountsDetails();
    Optional<AccountDetailsQuery> getAccountDetailById(AccountId accountId);
}
