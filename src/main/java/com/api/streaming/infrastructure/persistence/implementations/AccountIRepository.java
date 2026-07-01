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
import com.api.streaming.infrastructure.persistence.dtos.AccountDetailsProjection;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.repositories.AccountJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountIRepository implements AccountInterface, AccountQueriesInterface {

    private final AccountJpaRepository jpa;
    private final AccountMapper mapper;
    
    //External Repositories
    // private final PlatformJpaRepository platformJpaRepository;
    // private final PaymentJpaRepository paymentJpaRepository;
    // private final MembershipJpaRepository membershipJpaRepository;
    // private final MembershipConfigJpaRepository membershipConfigJpaRepository;

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
        // List<AccountEntity> accounts = this.jpa.findAll();

        // if (accounts.isEmpty())
        //     return Collections.emptyList();

        // Map<String, PlatformEntity> platforms = this.platformJpaRepository.findAll()
        //     .stream()
        //     .collect(Collectors.toMap(PlatformEntity::getId, Function.identity()));


        // return accounts
        // .stream()
        // .map(account -> {
        //     PlatformEntity platform = platforms.get(account.getPlatform());
        //     PaymentEntity payment = this.paymentJpaRepository.findById(account.getPayment()).get();

        //     MembershipEntity membershipEntity = this.membershipJpaRepository.findByAccount(account.getId()).get();
        //     MembershipConfigEntity membershipConfigEntity = this.membershipConfigJpaRepository.findById(membershipEntity.getMembershipConfig()).get();
        //     // MembershipDetailsQuery membershipDetails = new MembershipDetailsQuery(membershipEntity.getId(), membershipConfigEntity);
        //     String membershipId = membershipEntity.getId();

        //     return this.mapper.fromEntityToQuery(account, platform, payment, membershipId, membershipConfigEntity);
        // })
        // .collect(Collectors.toList());

        List<AccountDetailsProjection> accountsProjections = this.jpa.findAllAccountDetails();
        return this.mapper.fromProjectionListToQueryList(accountsProjections);
    }

    @Override
    public Optional<AccountDetailsQuery> getAccountDetailById(AccountId accountId) {

        return this.jpa.findAccountDetailsById(accountId.value())
                .map(projection -> {
                    return this.mapper.fromProjectionToQuery(projection);
                });
        // return this.jpa.findById(accountId.value())
        //         .map(entity -> {
        //             PlatformEntity platform = this.platformJpaRepository.findById(entity.getPlatform())
        //                                 .orElseThrow(() -> new EntityNotFoundException("Platform Entity not registered for this account."));
                    
        //             PaymentEntity payment = this.paymentJpaRepository.findById(entity.getPayment()).orElseThrow(() -> new EntityNotFoundException("Payment Entity not registered for this account."));

        //             return this.mapper.fromEntityToQuery(entity, platform, payment);
        //         });
    }



}
