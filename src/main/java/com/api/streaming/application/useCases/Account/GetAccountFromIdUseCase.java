package com.api.streaming.application.useCases.Account;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.valueObjects.AccountId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAccountFromIdUseCase {
    private final AccountQueriesInterface accountQueriesInterface;

    public AccountDetailsQuery execute(AccountId id) {
        return this.accountQueriesInterface.getAccountDetailById(id)
            .orElseThrow(() -> new EntityNotFoundException("This account doesn't exist."));
    }
}
