package com.api.streaming.application.useCases.Order;

import java.util.List;

import com.api.streaming.application.commands.UpdateOrderCommand;
import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.infrastructure.mappers.OrderMapper;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateOrderUseCase {
    private final OrderInterface orderInterface;
    private final OrderMapper mapper;

    // External services
    private final MembershipInterface membershipInterface;
    private final MembershipConfigInterface membershipConfigInterface;

    public OrderId execute(UpdateOrderCommand command) {
        Order orderToUpdate = this.checkIfOrderExists(command.id());

        if (command.profileAmount() != null)
            this.checkSlotProfilesAvailable(orderToUpdate.getAccount(), command.profileAmount(),
                    orderToUpdate.getOrderType(), orderToUpdate.getId());
        
        this.mapper.updateOrderFromCommand(command, orderToUpdate);

        this.orderInterface.saveOrder(orderToUpdate);
        return command.id();

    }

    private Order checkIfOrderExists(OrderId orderId) {
        return this.orderInterface.getOrderById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("You cannot create an order if account does not exist."));
    }

    private void checkSlotProfilesAvailable(AccountId accountId, Integer profilesRequired, OrderType orderType, OrderId orderId) {

        if (orderType.equals(OrderType.FULL))
            throw new IllegalArgumentException("You cannot update an order profile if this is FULL type.");

        if (profilesRequired != 0)
            throw new IllegalArgumentException("If you want update profiles, you have to put it more than 0");

        List<Order> accountWithOrdersFull = this.orderInterface.getOrdersByAccountIdAndOrderType(accountId,
                OrderType.FULL);

        if (accountWithOrdersFull.size() > 0)
            throw new EntityExistsException(
                    "You cannot add a profile order to an account that already has a full order.");

        Membership membership = this.membershipInterface.getMembershipByAccount(accountId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "To define new order, define first a membership for this account."));

        MembershipConfig config = this.membershipConfigInterface
                .getMembershipConfig(membership.getMembershipConfig())
                .orElseThrow(
                        () -> new EntityNotFoundException("No membership config defined for this account."));

        List<Order> accountsWithOrders = this.orderInterface.getOrdersByAccountIdAndOrderType(accountId,
                OrderType.PROFILE);

        Integer profiles = config.getProfiles();

        Integer usedProfiles = 0;
        for (Order order : accountsWithOrders) {
            if (order.getId() != orderId)
                usedProfiles += order.getProfileAmount();
        }

        usedProfiles += profilesRequired;

        if (usedProfiles > profiles)
            throw new IllegalArgumentException(
                    "You cannot assign this quantity of profiles to this account, because there are not enough slots.");

    }
}
