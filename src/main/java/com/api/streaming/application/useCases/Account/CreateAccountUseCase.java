package com.api.streaming.application.useCases.Account;

import java.time.LocalDate;
import java.util.List;

import com.api.streaming.application.commands.CreateAccountCommand;
import com.api.streaming.domain.enums.AccountStatus;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateAccountUseCase {
    private final AccountInterface accountInterface;
    private final PaymentInterface paymentInterface;
    private final PlatformInterface platformInterface;
    private final GenerateIdInterface generateIdInterface;

    public AccountId execute(CreateAccountCommand command) {

        // Validations
        this.checkIfPaymentExists(command.payment());
        this.checkIfPlatformExists(command.platform());
        this.checkIfEmailIsRegisteredWithSamePlatform(command.email(), command.platform());

        AccountId id = this.generateIdInterface.generateAccountId();
        AccountStatus initialStatus = AccountStatus.ACTIVE;
        LocalDate registeredAt = LocalDate.now();

        Account createdAccount = new Account(
            id, 
            command.email(), 
            command.password(), 
            registeredAt, 
            command.platform(), 
            command.payment(), 
            initialStatus
        );

        this.accountInterface.saveAccount(createdAccount);
        return id;
    }

    private void checkIfPaymentExists(PaymentId id) {
        this.paymentInterface.getPayment(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment ID does not exists."));
    }

    private void checkIfPlatformExists(PlatformId id) {
        this.platformInterface.getPlatform(id)
            .orElseThrow(() -> new EntityNotFoundException("Platform ID doesn't exists."));
    }

    private void checkIfEmailIsRegisteredWithSamePlatform(Email email, PlatformId platform) {

        List<Account> accounts = this.accountInterface.getAccountByEmailAndPlatform(email, platform);

        if (accounts.size() != 0)
            throw new EntityExistsException("There is already an account registered with this email for this platform.");
    }
}
