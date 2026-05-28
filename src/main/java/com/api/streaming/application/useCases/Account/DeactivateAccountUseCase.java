package com.api.streaming.application.useCases.Account;

import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeactivateAccountUseCase {
    private final AccountInterface accountInterface;

    public void execute(AccountId id) {
        Account account = this.checkAccount(id);
        account.deactivateAccount();
        this.accountInterface.saveAccount(account);
    }

    private Account checkAccount( AccountId id) {
        return this.accountInterface.getAccount(id)
            .orElseThrow(() -> new EntityNotFoundException("Account that you want deactivate doesn't exist."));
    }
}
