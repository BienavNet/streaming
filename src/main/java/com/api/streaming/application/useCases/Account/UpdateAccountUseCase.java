package com.api.streaming.application.useCases.Account;

import java.util.List;

import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.mappers.AccountMapper;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateAccountUseCase {
    private final AccountInterface accountInterface;
    private final PaymentInterface paymentInterface;
    private final PlatformInterface platformInterface;
    private final AccountMapper mapper;

    // Repo added to check if user wants to update platform, this match with membership registered
    private final MembershipInterface membershipInterface;
    

    public AccountId execute(UpdateAccountCommand command) {

        //Validations
        Account accountToUpdate = this.checkIfAccountExist(command.id());

        if (command.payment() != null)
            this.paymentExists(command.payment());
        

        if (command.platform() != null){
            this.checkIfMembershipIsBoundAndPlatformsMatch(accountToUpdate.getId());
            this.platformExists(command.platform());
        }
        
        if (command.platform() != null || command.email() != null)
            this.checkIfEmailIsRegisteredWithSamePlatform(command.email() != null ? command.email() : accountToUpdate.getEmail(), command.platform() != null ? command.platform() : accountToUpdate.getPlatform());


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

    private void checkIfEmailIsRegisteredWithSamePlatform(Email email, PlatformId platform) {

        List<Account> accounts = this.accountInterface.getAccountByEmailAndPlatform(email, platform);

        if (accounts.size() != 0)
            throw new EntityExistsException("There is already an account registered with this email for this platform.");
    }

    private void checkIfMembershipIsBoundAndPlatformsMatch(AccountId id) {
        this.membershipInterface.getMembershipByAccount(id)
            .ifPresent(e -> new EntityExistsException("Cannot update the platform for this account because this account has one membership registered."));
    }
}
