package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.DashboardInfoInterface;
import com.api.streaming.application.useCases.Dashboard.GetDashboardDetailsUseCase;

@Configuration
public class DashboardUseCasesConfig {
    
    @Bean
    GetDashboardDetailsUseCase getDashboardDetailsUseCase(
        DashboardInfoInterface dashboardInfoInterface
    ) {
        return new GetDashboardDetailsUseCase(dashboardInfoInterface);
    }
}
