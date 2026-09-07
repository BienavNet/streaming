package com.api.streaming.infrastructure.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.AccountQueriesInterface;
import com.api.streaming.application.interfaces.strategies.AccountFilterStrategy;
import com.api.streaming.application.useCases.Account.CreateAccountUseCase;
import com.api.streaming.application.useCases.Account.DeleteAccountUseCase;
import com.api.streaming.application.useCases.Account.FilterAccountUseCase;
import com.api.streaming.application.useCases.Account.GetAccountFromIdUseCase;
import com.api.streaming.application.useCases.Account.GetAllAccountsUseCase;
import com.api.streaming.application.useCases.Account.UpdateAccountUseCase;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.infrastructure.mappers.AccountMapper;

@Configuration
public class AccountUseCasesConfig {

    @Bean
    CreateAccountUseCase createAccountUseCase(
        AccountInterface accountInterface, 
        PaymentInterface paymentInterface,
        PlatformInterface platformInterface,
        GenerateIdInterface generateIdInterface
    ) {
        return new CreateAccountUseCase(accountInterface, paymentInterface, platformInterface, generateIdInterface);
    }

    @Bean
    DeleteAccountUseCase deleteAccountUseCase(
        AccountInterface accountInterface, 
        MembershipInterface membershipInterface,
        OrderInterface orderInterface
    ) {
        return new DeleteAccountUseCase(accountInterface, membershipInterface, orderInterface);
    }

    @Bean
    GetAccountFromIdUseCase deleteAccountFromIdUseCase(
        AccountQueriesInterface accountInterface
    ) {
        return new GetAccountFromIdUseCase(accountInterface);
    }

    @Bean
    GetAllAccountsUseCase getAllAccountsUseCase(
        AccountQueriesInterface accountInterface
    ) {
        return new GetAllAccountsUseCase(accountInterface);
    }

    @Bean
    UpdateAccountUseCase updateAccountUseCase(
        AccountInterface accountInterface,
        PaymentInterface paymentInterface,
        PlatformInterface platformInterface,
        AccountMapper mapper,
        MembershipInterface membershipInterface
    ) {
        return new UpdateAccountUseCase(accountInterface, paymentInterface, platformInterface, mapper, membershipInterface);
    }

    @Bean
    FilterAccountUseCase filterAccountUseCase(
        Map<String, AccountFilterStrategy> strategies
    ) {
        return new FilterAccountUseCase(strategies);
    }
}
