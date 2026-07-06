package com.api.streaming.application.useCases.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.UpdateOrderCommand;
import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.domain.interfaces.OrderInterface;
import com.api.streaming.domain.models.Membership;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.infrastructure.mappers.OrderMapper;

@ExtendWith(MockitoExtension.class)
public class UpdateOrderUTest {

    @Mock
    private OrderInterface orderInterface;
    @Mock
    private OrderMapper mapper;
    @Mock
    private MembershipInterface membershipInterface;
    @Mock
    private MembershipConfigInterface membershipConfigInterface;

    // Mocks de entidades
    @Mock
    private Order orderToUpdateMock;
    @Mock
    private Order otherOrderMock;
    @Mock
    private Membership membershipMock;
    @Mock
    private MembershipConfig membershipConfigMock;

    @InjectMocks
    private UpdateOrderUseCase useCase;

    private OrderId orderId;
    private OrderId otherOrderId;
    private AccountId accountId;
    private MembershipConfigId configId;

    @BeforeEach
    void setup() {
        this.orderId = new OrderId("uuid-ord-111");
        this.otherOrderId = new OrderId("uuid-ord-222");
        this.accountId = new AccountId("uuid-acc-123");
        this.configId = new MembershipConfigId("uuid-cfg-456");
    }

    @Test
    void shouldUpdateOrderSuccessfullyWhenProfileAmountIsZero() {
        // Given

        UpdateOrderCommand commandWithZeroProfiles = new UpdateOrderCommand(
            this.orderId, 0, LocalDate.now(), LocalDate.now().plusMonths(1), 49.99
        );

        when(this.orderToUpdateMock.getId()).thenReturn(this.orderId);
        when(this.orderToUpdateMock.getAccount()).thenReturn(this.accountId);

        when(this.orderInterface.getOrderById(this.orderId))
            .thenReturn(Optional.of(this.orderToUpdateMock));
        when(this.orderToUpdateMock.getOrderType()).thenReturn(OrderType.PROFILE);
        
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(Collections.emptyList());
        
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));
        when(this.membershipMock.getMembershipConfig()).thenReturn(this.configId);
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.of(this.membershipConfigMock));
        when(this.membershipConfigMock.getProfiles()).thenReturn(5);

        when(this.otherOrderMock.getId()).thenReturn(this.otherOrderId);
        when(this.otherOrderMock.getProfileAmount()).thenReturn(3);
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.PROFILE))
            .thenReturn(List.of(this.orderToUpdateMock, this.otherOrderMock));

        // When
        OrderId resultId = this.useCase.execute(commandWithZeroProfiles);

        // Then
        assertThat(resultId).isEqualTo(this.orderId);
        verify(this.orderInterface, times(1)).saveOrder(this.orderToUpdateMock);
    }

    @Test
    void shouldThrowExceptionWhenOrderToUpdateDoesNotExist() {
        // Given
        UpdateOrderCommand command = new UpdateOrderCommand(this.orderId, 2, LocalDate.now(), LocalDate.now(), 10.0);
        when(this.orderInterface.getOrderById(this.orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(command))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("You cannot create an order if account does not exist.");

        verify(this.orderInterface, never()).saveOrder(any());
    }

    @Test
    void shouldThrowExceptionWhenTryingToUpdateProfilesOnAFullOrder() {
        // Given
        UpdateOrderCommand command = new UpdateOrderCommand(this.orderId, 2, LocalDate.now(), LocalDate.now(), 10.0);
        when(this.orderInterface.getOrderById(this.orderId)).thenReturn(Optional.of(this.orderToUpdateMock));
        when(this.orderToUpdateMock.getOrderType()).thenReturn(OrderType.FULL);

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You cannot update an order profile if this is FULL type.");
    }

    @Test
    void shouldThrowExceptionWhenProfilesRequiredIsDifferentFromZero() {
        // Given
        UpdateOrderCommand command = new UpdateOrderCommand(this.orderId, 2, LocalDate.now(), LocalDate.now(), 10.0);
        when(this.orderInterface.getOrderById(this.orderId)).thenReturn(Optional.of(this.orderToUpdateMock));
        when(this.orderToUpdateMock.getOrderType()).thenReturn(OrderType.PROFILE);

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("If you want update profiles, you have to put it more than 0");
    }

    @Test
    void shouldThrowExceptionWhenAccountHasAFullOrderActive() {
        // Given
        UpdateOrderCommand command = new UpdateOrderCommand(this.orderId, 0, LocalDate.now(), LocalDate.now(), 10.0);
        when(this.orderToUpdateMock.getId()).thenReturn(this.orderId);
        when(this.orderToUpdateMock.getAccount()).thenReturn(this.accountId);
        
        when(this.orderInterface.getOrderById(this.orderId)).thenReturn(Optional.of(this.orderToUpdateMock));
        when(this.orderToUpdateMock.getOrderType()).thenReturn(OrderType.PROFILE);

        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(List.of(this.otherOrderMock));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(command))
            .isInstanceOf(EntityExistsException.class)
            .hasMessage("You cannot add a profile order to an account that already has a full order.");
    }

    @Test
    void shouldThrowExceptionWhenNewProfilesExceedConfigSlots() {
        // Given
        UpdateOrderCommand command = new UpdateOrderCommand(this.orderId, 0, LocalDate.now(), LocalDate.now(), 10.0);
        when(this.orderToUpdateMock.getId()).thenReturn(this.orderId);
        when(this.orderToUpdateMock.getAccount()).thenReturn(this.accountId);
        
        when(this.orderInterface.getOrderById(this.orderId)).thenReturn(Optional.of(this.orderToUpdateMock));
        when(this.orderToUpdateMock.getOrderType()).thenReturn(OrderType.PROFILE);

        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(Collections.emptyList());
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));
        when(this.membershipMock.getMembershipConfig()).thenReturn(this.configId);
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.of(this.membershipConfigMock));
        
        when(this.membershipConfigMock.getProfiles()).thenReturn(2); 

        when(this.otherOrderMock.getId()).thenReturn(this.otherOrderId);
        when(this.otherOrderMock.getProfileAmount()).thenReturn(3);
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.PROFILE))
            .thenReturn(List.of(this.orderToUpdateMock, this.otherOrderMock));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You cannot assign this quantity of profiles to this account, because there are not enough slots.");
    }
}

