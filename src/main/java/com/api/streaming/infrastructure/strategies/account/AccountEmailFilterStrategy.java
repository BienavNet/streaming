package com.api.streaming.infrastructure.strategies.account;

import java.util.List;

import org.springframework.stereotype.Component;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.interfaces.strategies.AccountFilterStrategy;
import com.api.streaming.application.queries.AccountDetailsQuery;

import lombok.AllArgsConstructor;

@Component("account_email")
@AllArgsConstructor
public class AccountEmailFilterStrategy implements AccountFilterStrategy {
    
    private final AccountQueriesInterface accountQueriesInterface;

    @Override
    public List<AccountDetailsQuery> filter(String criteria) {
        return this.accountQueriesInterface.findByEmail(criteria);
    }


}
