package com.api.streaming.application.useCases.Account;

import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.valueObjects.AccountId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteAccountUseCase {
    private final AccountInterface accountInterface;
    // Anclamos nuevas interfaces para ejecutar las validaciones del caso de uso 
    private final MembershipInterface membershipInterface;
    private final OrderInterface orderInterface;

    public void execute(AccountId id) {
        this.checkIfAccountExists(id);
        // Metemos la nueva validacion de cascade
        this.cascadeEntitiesValidation(id);
        this.accountInterface.deleteAccount(id);
    }

    private void checkIfAccountExists(AccountId id) {
        this.accountInterface.getAccount(id)
            .orElseThrow(() -> new EntityNotFoundException("Account that you're trying to delete doesn't exist."));
    }

    private void cascadeEntitiesValidation(AccountId accountId) {
        // Esta entidad tiene relacion con las entidades de: membership, order
        this.membershipInterface.getMembershipByAccount(accountId)
            .ifPresent(entity -> new EntityExistsException("Cannot delete this account because there's one membership registered to this. Please delete first this membership."));

        if (!this.orderInterface.getOrdersByAccountId(accountId).isEmpty())
            throw new EntityExistsException("Cannot delete this account because there's orders registered to this. Please remove all orders from this account. Remember, orders history never will be deleted and you can query it whenever you want.");
    }
}
