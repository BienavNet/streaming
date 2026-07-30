package com.api.streaming.application.useCases.Membership;

import java.util.List;

import com.api.streaming.application.commands.UpdateMembershipCommand;
import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.infrastructure.mappers.MembershipMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateMembershipUseCase {
    private final MembershipInterface membershipInterface;
    private final MembershipMapper mapper;

    // extendemos la funcionalidad para validar cuando se quiera cambiar el plan o el membershipConfig
    private final MembershipConfigInterface configInterface;
    private final OrderInterface orderInterface;


    public MembershipId execute(UpdateMembershipCommand command) {

        
        Membership membershipToUpdate = this.checkIfMembershipExists(command.membershipId());
        
        if (!command.membershipConfigId().value().isEmpty())
            this.checkIfMembershipConfigIsCorrect(command.membershipConfigId(), membershipToUpdate.getMembershipConfig(), membershipToUpdate.getAccount());

        this.mapper.updateMembershipFromCommand(command, membershipToUpdate);
        this.membershipInterface.saveMembership(membershipToUpdate);

        return membershipToUpdate.getId();
    }

    private Membership checkIfMembershipExists(MembershipId membershipId) {
        return this.membershipInterface.getMembership(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot update membership because that does not exist."));
    }

    private void checkIfMembershipConfigIsCorrect(MembershipConfigId newPlan, MembershipConfigId currentPlan, AccountId accountId) {
        MembershipConfig newConfig = this.configInterface.getMembershipConfig(newPlan)
            .orElseThrow(() -> new EntityNotFoundException("None plan is found with this id."));

        MembershipConfig currentConfig = this.configInterface.getMembershipConfig(currentPlan)
            .orElseThrow(() -> new EntityNotFoundException("None plan is found with this id."));

        List<Order> orders = this.orderInterface.getOrdersByAccountId(accountId);
        Integer orderProfilesUsed = 0;
        

        if (orders.size() == 1)
            if (orders.get(0).getOrderType() == OrderType.FULL)
                orderProfilesUsed = currentConfig.getProfiles();
            else
                orderProfilesUsed = orders.get(0).getProfileAmount();

        if (orders.size() > 1){
            for (Order order : orders) {
                orderProfilesUsed += order.getProfileAmount();
            }
        }

        // validaciones con las ordenes a ver si aunque el nuevo profiles llega a ser menor, se pueda asignar si las ordenes no estan completas
        if (orderProfilesUsed >= newConfig.getProfiles())
            throw new IllegalArgumentException("Cannot update new plan because new profile quantity is minor that current current profiles used.");
    }
}
