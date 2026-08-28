package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

public interface AccountInterface {
    Optional<Account> getAccount(AccountId accountId);
    List<Account> getAccounts();
    List<Account> getAccountsByPlatform(PlatformId id);
    List<Account> getAccountsByPaymentId(PaymentId id);
    Account saveAccount(Account account);
    void deleteAccount(AccountId accountId);

    List<Account> getAccountByEmailAndPlatform(Email email, PlatformId platformId);
}
