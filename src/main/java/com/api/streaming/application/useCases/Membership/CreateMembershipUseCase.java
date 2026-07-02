package com.api.streaming.application.useCases.Membership;

import java.util.Optional;

import com.api.streaming.application.commands.CreateMembershipCommand;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateMembershipUseCase {
    private final MembershipInterface membershipInterface;
    private final GenerateIdInterface generateIdInterface;

    //Externals services
    private final AccountInterface accountInterface;
    private final MembershipConfigInterface membershipConfigInterface;

    public MembershipId execute(CreateMembershipCommand command) {
        this.checkIfAccountExists(command.account());
        this.checkIfAccountMembershipIsNotAlreadyDefined(command.account());
        this.checkIfAccountPlatformMatchWithMembershipConfigPlatform(command.account(), command.membershipConfig());

        MembershipId id = this.generateIdInterface.generatMembershipId();

        Membership membership = new Membership(
            id, 
            command.account(), 
            command.membershipConfig(),
            command.isFreeTrial(), 
            command.membershipBuyDate(), 
            command.membershipExpireDate()
        );

        this.membershipInterface.saveMembership(membership);

        return id;
    }

    private void checkIfAccountExists(AccountId accountId){
        this.accountInterface.getAccount(accountId)
            .orElseThrow(() -> new EntityNotFoundException("This account does not exists."));
    }

    private void checkIfAccountMembershipIsNotAlreadyDefined(AccountId accountId) {
        Optional<Membership> membershipOptional = this.membershipInterface.getMembershipByAccount(accountId);

        if(membershipOptional.isPresent())
            throw new EntityExistsException("This account already has a membership registered.");
    }

    private void checkIfAccountPlatformMatchWithMembershipConfigPlatform(AccountId account, MembershipConfigId membershipConfig) {
        MembershipConfig config = this.membershipConfigInterface.getMembershipConfig(membershipConfig).orElseThrow(() -> new EntityNotFoundException("This membership config does not exists, so couldn't attach to this membership."));

        Account acc = this.accountInterface.getAccount(account).orElseThrow(() -> new EntityNotFoundException("You cannot attach a membership to an account that does not exist."));

        if (!acc.getPlatform().equals(config.getPlatform())) {
            throw new IllegalArgumentException("Your platform account must be match with membership platform.");
        }
    }


}
