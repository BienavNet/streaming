package com.api.streaming.application.useCases.Order;

import java.time.LocalDate;
import java.util.List;

import com.api.streaming.application.commands.CreateOrderCommand;
import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateOrderUseCase {
    private final OrderInterface orderInterface;
    private final GenerateIdInterface generateIdInterface;

    // external services
    private final MembershipConfigInterface membershipConfigInterface;
    private final MembershipInterface membershipInterface;

    public OrderId execute(CreateOrderCommand command) {
        // business validations
        this.checkIfAccountExistsAndIsActive(command.account());

        this.checkSlotProfilesAvailable(command.account(), command.profileAmount(), command.orderType());

        OrderId id = this.generateIdInterface.generateOrderId();

        Order newOrder = new Order(id, command.account(), command.profileAmount(), command.buyDate(),
                command.finishDate(), command.paymentAmount(), command.orderType());

        this.orderInterface.saveOrder(newOrder);

        return id;

    }

    private void checkIfAccountExistsAndIsActive(AccountId accountId) {
        Membership membership = this.membershipInterface.getMembershipByAccount(accountId)
                .orElseThrow(
                        () -> new EntityNotFoundException("You cannot create an order if this account has not a membership."));

        if (membership.getMembershipExpireDate().isBefore(LocalDate.now()))
                throw new IllegalArgumentException("You cannot create a new order with an expired memberhsip.");
    }

    private void checkSlotProfilesAvailable(AccountId accountId, int profilesRequired, OrderType orderType) {

        if (orderType.equals(OrderType.PROFILE)) {
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
                    .orElseThrow(() -> new EntityNotFoundException("No membership config defined for this account."));

            List<Order> accountsWithOrders = this.orderInterface.getOrdersByAccountIdAndOrderType(accountId,
                    OrderType.PROFILE);

            int profiles = config.getProfiles();

            int usedProfiles = 0;
            for (Order order : accountsWithOrders) {
                usedProfiles += order.getProfileAmount();
            }

            usedProfiles += profilesRequired;

            if (usedProfiles > profiles)
                throw new IllegalArgumentException(
                        "You cannot assign this quantity of profiles to this account, because there are not enough slots.");
        } else {
            List<Order> accountWithOrdersFull = this.orderInterface.getOrdersByAccountIdAndOrderType(accountId,
                    OrderType.FULL);

            if (accountWithOrdersFull.size() > 0)
                throw new EntityExistsException(
                        "You cannot add a full order to an account that already has been sold full.");

            List<Order> accountsWithOrders = this.orderInterface.getOrdersByAccountIdAndOrderType(accountId,
                    OrderType.PROFILE);

            if (accountsWithOrders.size() > 0)
                throw new EntityExistsException(
                        "You cannot add a full order to this account, because you already has been this account by Profiles.");
        }
    }
}
