package com.api.streaming.application.useCases.Account;


import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAllAccountsUseCase {
    private final AccountQueriesInterface accountQueriesInterface;

    public Page<AccountDetailsQuery> execute(Pageable pageable) {

        if (pageable == null)
            pageable = new Pageable(0, 10);

        Page<AccountDetailsQuery> page = this.accountQueriesInterface.getAccountsDetails(pageable);

        return page;
    }
}
