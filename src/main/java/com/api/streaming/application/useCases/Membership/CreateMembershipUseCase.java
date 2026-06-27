package com.api.streaming.application.useCases.Membership;

import java.util.List;

import com.api.streaming.application.commands.CreateMembershipCommand;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.models.Membership;
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

    public MembershipId execute(CreateMembershipCommand command) {
        this.checkIfAccountExists(command.account());
        this.checkIfAccountMembershipIsNotAlreadyDefined(command.account(), command.membershipConfig());

        MembershipId id = this.generateIdInterface.generatMembershipId();

        Membership membership = new Membership(id, command.account(), command.membershipConfig());

        this.membershipInterface.saveMembership(membership);

        return id;
    }

    private void checkIfAccountExists(AccountId accountId){
        this.accountInterface.getAccount(accountId)
            .orElseThrow(() -> new EntityNotFoundException("This account does not exists."));
    }

    private void checkIfAccountMembershipIsNotAlreadyDefined(AccountId accountId, MembershipConfigId membershipConfigId) {

        List<Membership> list = this.membershipInterface.getMembershipsByAccount(accountId);
        
        for (Membership item : list) {
            if(item.getMembershipConfig().equals(membershipConfigId))
                throw new EntityExistsException("Is already registered an account with this membership.");
        }
    }


}
