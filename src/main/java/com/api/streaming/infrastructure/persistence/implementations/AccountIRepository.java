package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.AccountMapper;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.projections.AccountDetailsProjection;
import com.api.streaming.infrastructure.persistence.repositories.AccountJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountIRepository implements AccountInterface, AccountQueriesInterface {

    private final AccountJpaRepository jpa;
    private final AccountMapper mapper;

    @Override
    public Optional<Account> getAccount(AccountId accountId) {
        return this.jpa.findById(accountId.value())
            .map(this.mapper::fromEntityToDomain);
    }

    @Override
    public List<Account> getAccounts() {
        List<AccountEntity> entities = this.jpa.findAll();
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public List<Account> getAccountsByPlatform(PlatformId id) {
        List<AccountEntity> entities = this.jpa.findByPlatform(id.value());
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public Account saveAccount(Account account) {
        AccountEntity savedEntity = this.jpa.saveAndFlush(this.mapper.fromDomainToEntity(account));
        return this.mapper.fromEntityToDomain(savedEntity);
    }

    @Override
    public void deleteAccount(AccountId accountId) {
       this.jpa.deleteById(accountId.value());
    }

    @Override
    public List<Account> getAccountByEmailAndPlatform(Email email, PlatformId platformId) {
        List<AccountEntity> entities = this.jpa.findByEmailAndPlatform(email.value(), platformId.value());
        return this.mapper.fromEntityListToDomainList(entities);
    }

    @Override
    public List<AccountDetailsQuery> getAccountsDetails() {
        List<AccountDetailsProjection> accountsProjections = this.jpa.findAllAccountDetails();
        return this.mapper.fromProjectionListToQueryList(accountsProjections);
    }

    @Override
    public Optional<AccountDetailsQuery> getAccountDetailById(AccountId accountId) {

        return this.jpa.findAccountDetailsById(accountId.value())
                .map(projection -> {
                    return this.mapper.fromProjectionToQuery(projection);
                });
    }



}
