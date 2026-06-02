package com.api.streaming.application.useCases.Payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.streaming.domain.interfaces.PaymentInterface;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class GetPaymentDetailsUTest {
    
    @Mock
    private PaymentInterface paymentInterface;

    @InjectMocks
    private GetPaymentDetailsUseCase getPaymentDetailsUseCase;

    private PaymentId expectedId;
    private Payment expectedPayment;

    @BeforeEach
    void setup() {
        this.expectedId = new PaymentId("uuid-23984");
        this.expectedPayment = new Payment(this.expectedId, "4242424242424224", 10, 2026, "123", "Banco de Colombia");
    }
    
    @Test
    void shouldGetPaymentDetailsSuccessfully() {
        when(this.paymentInterface.getPayment(this.expectedId)).thenReturn(Optional.of(this.expectedPayment));

        Payment resultPayment = this.getPaymentDetailsUseCase.execute(this.expectedId);

        assertThat(resultPayment)
        .isInstanceOf(Payment.class)
        .isEqualTo(this.expectedPayment);
    }

    @Test
    void shouldThrowExceptionWhenThisDoesNotExist() {
        when(this.paymentInterface.getPayment(this.expectedId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.getPaymentDetailsUseCase.execute(this.expectedId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Payment not found.");
    }


}
