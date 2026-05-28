package com.api.streaming.application.useCases.Account;

import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.valueObjects.AccountId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteAccountUseCase {
    private final AccountInterface accountInterface;

    public void execute(AccountId id) {
        //Validations 
        this.checkIfAccountExists(id);
        this.accountInterface.deleteAccount(id);
    }

    private void checkIfAccountExists(AccountId id) {
        this.accountInterface.getAccount(id)
            .orElseThrow(() -> new EntityNotFoundException("Account that you're trying to delete doesn't exist."));
    }
}
