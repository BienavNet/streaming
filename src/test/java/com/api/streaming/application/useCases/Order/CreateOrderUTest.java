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
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.OrderId;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUTest {

    @Mock
    private OrderInterface orderInterface;
    @Mock
    private GenerateIdInterface generateIdInterface;
    @Mock
    private MembershipConfigInterface membershipConfigInterface;
    @Mock
    private MembershipInterface membershipInterface;

    @Mock
    private Membership membershipMock;
    @Mock
    private MembershipConfig membershipConfigMock;
    @Mock
    private Order orderMock;

    @InjectMocks
    private CreateOrderUseCase useCase;

    private AccountId accountId;
    private MembershipConfigId configId;
    private OrderId newOrderId;
    private CreateOrderCommand baseProfileCommand;
    private CreateOrderCommand baseFullCommand;

    @BeforeEach
    void setup() {
        this.accountId = new AccountId("uuid-acc-123");
        this.configId = new MembershipConfigId("uuid-cfg-456");
        this.newOrderId = new OrderId("uuid-ord-999");

        // Comando base para Tipo PROFILE
        this.baseProfileCommand = new CreateOrderCommand(
            this.accountId,
            2,
            LocalDate.now(),
            LocalDate.now().plusMonths(1),
            29.99,
            OrderType.PROFILE
        );

        // Comando base para Tipo FULL
        this.baseFullCommand = new CreateOrderCommand(
            this.accountId,
            5,
            LocalDate.now(),
            LocalDate.now().plusMonths(1),
            59.99,
            OrderType.FULL
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountHasNoMembership() {
        // Given
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.baseProfileCommand))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("You cannot create an order if this account has not a membership.");

        verify(this.orderInterface, never()).saveOrder(any());
    }

    @Test
    void shouldThrowExceptionWhenMembershipIsExpired() {
        // Given
        LocalDate expiredDate = LocalDate.now().minusDays(1);
        when(this.membershipMock.getMembershipExpireDate()).thenReturn(expiredDate);
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.baseProfileCommand))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You cannot create a new order with an expired memberhsip.");

        verify(this.orderInterface, never()).saveOrder(any());
    }

    @Test
    void shouldCreateProfileOrderSuccessfully() {
        // Given
        when(this.membershipMock.getMembershipConfig()).thenReturn(this.configId);
        when(this.membershipMock.getMembershipExpireDate()).thenReturn(LocalDate.now().plusDays(10));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));
        
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(Collections.emptyList());
        
        when(this.membershipConfigInterface.getMembershipConfig(this.configId))
            .thenReturn(Optional.of(this.membershipConfigMock));
        when(this.membershipConfigMock.getProfiles()).thenReturn(5);

        // when(this.orderMock.getProfileAmount()).thenReturn(2);
        // when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.PROFILE))
        //     .thenReturn(List.of(this.orderMock));

        when(this.generateIdInterface.generateOrderId()).thenReturn(this.newOrderId);

        // When
        OrderId resultId = this.useCase.execute(this.baseProfileCommand);

        // Then
        assertThat(resultId).isEqualTo(this.newOrderId);
        verify(this.orderInterface, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenProfileOrderIsAddedToAccountWithFullOrder() {
        // Given
        when(this.membershipMock.getMembershipExpireDate()).thenReturn(LocalDate.now().plusDays(10));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));

        // Ya cuenta con una orden FULL activa
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(List.of(this.orderMock));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.baseProfileCommand))
            .isInstanceOf(EntityExistsException.class)
            .hasMessage("You cannot add a profile order to an account that already has a full order.");

        verify(this.orderInterface, never()).saveOrder(any());
    }

    @Test
    void shouldThrowExceptionWhenProfileOrderExceedsAvailableSlots() {
        // Given
        when(this.membershipMock.getMembershipConfig()).thenReturn(this.configId);

        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));
            
        when(this.membershipMock.getMembershipExpireDate()).thenReturn(LocalDate.now().plusDays(10));

        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(Collections.emptyList());

        when(this.membershipConfigInterface.getMembershipConfig(this.membershipMock.getMembershipConfig()))
            .thenReturn(Optional.of(this.membershipConfigMock));
        
        when(this.membershipConfigMock.getProfiles()).thenReturn(3);
        when(this.orderMock.getProfileAmount()).thenReturn(2);

        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.PROFILE))
            .thenReturn(List.of(this.orderMock));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.baseProfileCommand))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You cannot assign this quantity of profiles to this account, because there are not enough slots.");

        verify(this.orderInterface, never()).saveOrder(any());
    }

    @Test
    void shouldCreateFullOrderSuccessfully() {
        // Given
        when(this.membershipMock.getMembershipExpireDate()).thenReturn(LocalDate.now().plusDays(10));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));

        // No tiene órdenes históricas de ningún tipo
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(Collections.emptyList());
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.PROFILE))
            .thenReturn(Collections.emptyList());

        when(this.generateIdInterface.generateOrderId()).thenReturn(this.newOrderId);

        // When
        OrderId resultId = this.useCase.execute(this.baseFullCommand);

        // Then
        assertThat(resultId).isEqualTo(this.newOrderId);
        verify(this.orderInterface, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenFullOrderIsAddedToAccountAlreadySoldFull() {
        // Given
        when(this.membershipMock.getMembershipExpireDate()).thenReturn(LocalDate.now().plusDays(10));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));

        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(List.of(this.orderMock));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.baseFullCommand))
            .isInstanceOf(EntityExistsException.class)
            .hasMessage("You cannot add a full order to an account that already has been sold full.");

        verify(this.orderInterface, never()).saveOrder(any());
    }

    @Test
    void shouldThrowExceptionWhenFullOrderIsAddedToAccountWithProfiles() {
        // Given
        when(this.membershipMock.getMembershipExpireDate()).thenReturn(LocalDate.now().plusDays(10));
        when(this.membershipInterface.getMembershipByAccount(this.accountId))
            .thenReturn(Optional.of(this.membershipMock));
        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.FULL))
            .thenReturn(Collections.emptyList());

        when(this.orderInterface.getOrdersByAccountIdAndOrderType(this.accountId, OrderType.PROFILE))
            .thenReturn(List.of(this.orderMock));

        // When & Then
        assertThatThrownBy(() -> this.useCase.execute(this.baseFullCommand))
            .isInstanceOf(EntityExistsException.class)
            .hasMessage("You cannot add a full order to this account, because you already has been this account by Profiles.");

        verify(this.orderInterface, never()).saveOrder(any());
    }
}
