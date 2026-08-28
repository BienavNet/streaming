package com.api.streaming.infrastructure.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.PaymentFilterStrategy;
import com.api.streaming.application.useCases.Payment.CreatePaymentUseCase;
import com.api.streaming.application.useCases.Payment.DeletePaymentUseCase;
import com.api.streaming.application.useCases.Payment.FilterPaymentsUseCase;
import com.api.streaming.application.useCases.Payment.GetPaymentDetailsUseCase;
import com.api.streaming.application.useCases.Payment.GetPaymentsUseCase;
import com.api.streaming.domain.interfaces.AccountInterface;
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
        PaymentInterface paymentInterface,
        AccountInterface accountInterface
    ) {
        return new DeletePaymentUseCase(paymentInterface, accountInterface);
    }

    @Bean
    GetPaymentDetailsUseCase getPaymentDetailsUseCase(
        PaymentInterface paymentInterface
    ) {
        return new GetPaymentDetailsUseCase(paymentInterface);
    }

    @Bean
    GetPaymentsUseCase getPaymentsUseCase(
        PaymentInterface paymentInterface
    ) {
        return new GetPaymentsUseCase(paymentInterface);
    }

    @Bean
    FilterPaymentsUseCase filterPaymentsUseCase(
        Map<String, PaymentFilterStrategy> strategies
    ) {
        return new FilterPaymentsUseCase(strategies);
    }
}
