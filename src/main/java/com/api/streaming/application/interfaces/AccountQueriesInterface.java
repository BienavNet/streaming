package com.api.streaming.application.interfaces;

import java.util.Optional;

import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

public interface AccountQueriesInterface {
    Page<AccountDetailsQuery> getAccountsDetails(Pageable pageable);
    Optional<AccountDetailsQuery> getAccountDetailById(AccountId accountId);
}
