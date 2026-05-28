package com.api.streaming.application.useCases.Account;

import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.AccountMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateAccountUseCase {
    private final AccountInterface accountInterface;
    private final PaymentInterface paymentInterface;
    private final PlatformInterface platformInterface;
    private final AccountMapper mapper;

    public AccountId execute(UpdateAccountCommand command) {

        //Validations
        this.paymentExists(command.payment());
        this.platformExists(command.platform());

        Account accountToUpdate = this.checkIfAccountExist(command.id());
        this.mapper.updateAccountFromCommand(command, accountToUpdate);

        this.accountInterface.saveAccount(accountToUpdate);
        return command.id();
    }

    private Account checkIfAccountExist(AccountId id){
        return this.accountInterface.getAccount(id)
            .orElseThrow(() -> new EntityNotFoundException("Account you're trying to update doesn't exist."));
    }

    private void paymentExists(PaymentId id) {
        this.paymentInterface.getPayment(id)
            .orElseThrow(() -> new EntityNotFoundException("New payment is not registered yet."));
    }

    private void platformExists(PlatformId id) {
        this.platformInterface.getPlatform(id)
            .orElseThrow(() -> new EntityNotFoundException("New platform is not registered yet."));
    }
}
