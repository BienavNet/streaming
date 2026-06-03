package com.api.streaming.infrastructure.persistence.implementations;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.AccountMapper;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;
import com.api.streaming.infrastructure.persistence.repositories.AccountJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.PaymentJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.PlatformJpaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountIRepository implements AccountInterface, AccountQueriesInterface {

    private final AccountJpaRepository jpa;
    private final PlatformJpaRepository platformJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;
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
    public List<AccountDetailsQuery> getAccountsDetails() {
        List<AccountEntity> accounts = this.jpa.findAll();

        if (accounts.isEmpty())
            return Collections.emptyList();

        Map<String, PlatformEntity> platforms = this.platformJpaRepository.findAll()
            .stream()
            .collect(Collectors.toMap(PlatformEntity::getId, Function.identity()));


        return accounts
        .stream()
        .map(account -> {
            PlatformEntity platform = platforms.get(account.getPlatform());
            PaymentEntity payment = this.paymentJpaRepository.findById(account.getPayment()).get();

            return this.mapper.fromEntityToQuery(account, platform, payment);
        })
        .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountDetailsQuery> getAccountDetailById(AccountId accountId) {

        return this.jpa.findById(accountId.value())
                .map(entity -> {
                    PlatformEntity platform = this.platformJpaRepository.findById(entity.getPlatform())
                                        .orElseThrow(() -> new EntityNotFoundException("Platform Entity not registered for this account."));
                    
                    PaymentEntity payment = this.paymentJpaRepository.findById(entity.getPayment()).orElseThrow(() -> new EntityNotFoundException("Payment Entity not registered for this account."));

                    return this.mapper.fromEntityToQuery(entity, platform, payment);
                });
    }


}
