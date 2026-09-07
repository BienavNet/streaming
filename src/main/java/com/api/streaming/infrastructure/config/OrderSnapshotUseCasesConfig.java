package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.OrderDetailsInterface;
import com.api.streaming.application.interfaces.OrderDetailsSnapshotInterface;
import com.api.streaming.application.useCases.OrderDetailsSnapshot.GetSnapshotUseCase;
import com.api.streaming.application.useCases.OrderDetailsSnapshot.GetSnapshotsUseCase;
import com.api.streaming.application.useCases.OrderDetailsSnapshot.SaveOrderSnapshotUseCase;

@Configuration
public class OrderSnapshotUseCasesConfig {
    
    @Bean
    GetSnapshotUseCase getSnapshotUseCase(
        OrderDetailsSnapshotInterface snapshotInterface
    ) {
        return new GetSnapshotUseCase(snapshotInterface);
    }

    @Bean
    GetSnapshotsUseCase getSnapshotsUseCase(
        OrderDetailsSnapshotInterface snapshotInterface
    ) {
        return new GetSnapshotsUseCase(snapshotInterface);
    }

    @Bean
    SaveOrderSnapshotUseCase saveOrderSnapshotUseCase(
        OrderDetailsSnapshotInterface snapshotInterface,
        OrderDetailsInterface orderInterface
    ) {
        return new SaveOrderSnapshotUseCase(snapshotInterface, orderInterface);
    }
}
