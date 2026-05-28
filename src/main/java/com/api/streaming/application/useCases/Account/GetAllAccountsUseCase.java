package com.api.streaming.application.useCases.Account;

import java.util.List;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.queries.AccountDetailsQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAllAccountsUseCase {
    private final AccountQueriesInterface accountQueriesInterface;

    public List<AccountDetailsQuery> execute() {
        return this.accountQueriesInterface.getAccountsDetails();
    }
}
