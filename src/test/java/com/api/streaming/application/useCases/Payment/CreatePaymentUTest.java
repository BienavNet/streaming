package com.api.streaming.application.useCases.Payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.application.commands.CreatePaymentCommand;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;

import jakarta.persistence.EntityExistsException;

@ExtendWith(MockitoExtension.class)
public class CreatePaymentUTest {
    
    @Mock
    private PaymentInterface paymentInterface;

    @Mock
    private GenerateIdInterface generateIdInterface;

    @InjectMocks
    private CreatePaymentUseCase createPaymentUseCase;

    private CreatePaymentCommand command;
    private PaymentId expectedId;

    @BeforeEach
    void setup() {
        this.command = new CreatePaymentCommand("4242424242424224", 10, 2026, "123", "Banco de Colombia");

        this.expectedId = new PaymentId("uuid-8923");
    }

    @Test
    void shouldCreatePaymentSuccesfully() {
        when(this.generateIdInterface.generatePaymentId()).thenReturn(this.expectedId);

        PaymentId resultId = this.createPaymentUseCase.execute(this.command);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(this.paymentInterface, times(1)).savePayment(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        Payment expectedPayment = new Payment(expectedId, this.command.number(), this.command.month(), this.command.year(), this.command.cvv(), this.command.bankName());

        assertThat(resultId)
        .isNotNull()
        .isEqualTo(this.expectedId);

        assertThat(savedPayment)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(expectedPayment);
    }

    @Test
    void shouldThrowExceptionWhenNumberIsAlreadyRegistered() {
        Payment payment = new Payment(expectedId, "4554544545545445", 10, 2026, "123", null);
        when(this.paymentInterface.getPaymentByNumber(anyString())).thenReturn(Optional.of(payment));

        assertThatThrownBy(() -> this.createPaymentUseCase.execute(command))
        .isInstanceOf(EntityExistsException.class)
        .hasMessage("A payment has been registered with this number and you cannot register it again.");

    }

}
