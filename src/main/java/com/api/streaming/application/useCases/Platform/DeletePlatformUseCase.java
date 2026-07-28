package com.api.streaming.application.useCases.Platform;

import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeletePlatformUseCase {
    private final PlatformInterface platformInterface;

    // Repo for entities bound for deletion: membershipConfig, account

    private final AccountInterface accountInterface;
    private final MembershipConfigInterface membershipConfigInterface;

    public void execute(PlatformId id) {
        this.checkIfPlatformExists(id);
        this.cascadeEntitiesValidation(id);
        this.platformInterface.deletePlatform(id);
    }

    private void checkIfPlatformExists(PlatformId id) {
        this.platformInterface.getPlatform(id)
            .orElseThrow(() -> new EntityNotFoundException("Platform does not exist."));
    }

    private void cascadeEntitiesValidation(PlatformId id) {
        if (!this.accountInterface.getAccountsByPlatform(id).isEmpty())
            throw new EntityExistsException("Cannot delete this platform because there're account bound to this.");

        if (!this.membershipConfigInterface.getMembershipConfigsByPlatform(id).isEmpty())
            throw new EntityExistsException("Cannot delete this platform because there're plans bound to this.");
        
    }
}
