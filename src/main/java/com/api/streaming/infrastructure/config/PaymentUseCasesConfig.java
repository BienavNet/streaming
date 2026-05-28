package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.useCases.Payment.CreatePaymentUseCase;
import com.api.streaming.application.useCases.Payment.DeletePaymentUseCase;
import com.api.streaming.application.useCases.Payment.GetPaymentDetailsUseCase;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.PaymentInterface;

@Configuration
public class PaymentUseCasesConfig {
    
    @Bean
    CreatePaymentUseCase createPaymentUseCase(
        PaymentInterface paymentInterface,
        GenerateIdInterface generateIdInterface
    ) {
        return new CreatePaymentUseCase(paymentInterface, generateIdInterface);
    }

    @Bean
    DeletePaymentUseCase deletePaymentUseCase(
        PaymentInterface paymentInterface
    ) {
        return new DeletePaymentUseCase(paymentInterface);
    }

    @Bean
    GetPaymentDetailsUseCase getPaymentDetailsUseCase(
        PaymentInterface paymentInterface
    ) {
        return new GetPaymentDetailsUseCase(paymentInterface);
    }
}
