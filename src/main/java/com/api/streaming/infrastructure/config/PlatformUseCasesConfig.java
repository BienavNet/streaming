package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.useCases.Platform.CreatePlatformUseCase;
import com.api.streaming.application.useCases.Platform.DeletePlatformUseCase;
import com.api.streaming.application.useCases.Platform.GetAllPlatformsUseCase;
import com.api.streaming.application.useCases.Platform.GetPlatformUseCase;
import com.api.streaming.application.useCases.Platform.UpdatePlatformUseCase;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.infrastructure.mappers.PlatformMapper;

@Configuration
public class PlatformUseCasesConfig {
    
    @Bean
    CreatePlatformUseCase createPlatformUseCase(
        PlatformInterface platformRepository, 
        GenerateIdInterface generateIdService
    ) {
        return new CreatePlatformUseCase(platformRepository, generateIdService);
    }

    @Bean
    DeletePlatformUseCase deletePlatformUseCase(
        PlatformInterface platformInterface,
        AccountInterface accountInterface,
        MembershipConfigInterface membershipConfigInterface
    ) {
        return new DeletePlatformUseCase(platformInterface, accountInterface, membershipConfigInterface);
    }

    @Bean
    GetPlatformUseCase getPlatformUseCase(
        PlatformInterface platformInterface
    ) {
        return new GetPlatformUseCase(platformInterface);
    }

    @Bean
    GetAllPlatformsUseCase getAllPlatformsUseCase(
        PlatformInterface platformInterface
    ) {
        return new GetAllPlatformsUseCase(platformInterface);
    }

    @Bean
    UpdatePlatformUseCase updatePlatformUseCase(
        PlatformInterface platformInterface, 
        PlatformMapper mapper
    ) {
        return new UpdatePlatformUseCase(platformInterface, mapper);
    }
}
