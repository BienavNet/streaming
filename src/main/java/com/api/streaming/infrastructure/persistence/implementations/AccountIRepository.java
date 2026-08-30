package com.api.streaming.infrastructure.persistence.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.Page;
import com.api.streaming.domain.valueObjects.Pageable;
import com.api.streaming.domain.valueObjects.PaymentId;
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
    public Optional<AccountDetailsQuery> getAccountDetailById(AccountId accountId) {

        return this.jpa.findAccountDetailsById(accountId.value())
                .map(projection -> {
                    return this.mapper.fromProjectionToQuery(projection);
                });
    }

    @Override
    public Page<AccountDetailsQuery> getAccountsDetails(Pageable pageable) {
        org.springframework.data.domain.Pageable springPageable = PageRequest.of(pageable.getPage(), pageable.getOffset());
        org.springframework.data.domain.Page<AccountDetailsProjection> springPage = this.jpa.findAllAccountDetails(springPageable);

        List<AccountDetailsQuery> content = this.mapper.fromProjectionListToQueryList(springPage.getContent());

        return new Page<>(content, springPage.getTotalElements(), springPage.getTotalPages());
    }

    @Override
    public List<AccountDetailsQuery> findByEmail(String email) {
        List<AccountDetailsProjection> projections = this.jpa.findByEmailContainingIgnoreCase(email);

        return this.mapper.fromProjectionListToQueryList(projections);
    }

    @Override
    public List<AccountDetailsQuery> findByPlatform(String platform) {
        List<AccountDetailsProjection> projections = this.jpa.findByPlatformNameContainingIgnoreCase(platform);

        return this.mapper.fromProjectionListToQueryList(projections);
    }

    @Override
    public List<Account> getAccountsByPaymentId(PaymentId id) {
        List<AccountEntity> results = this.jpa.findByPayment(id.value());

        return this.mapper.fromEntityListToDomainList(results);
    }



}
