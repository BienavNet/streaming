package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.useCases.MembershipConfig.CreateMembershipConfigUseCase;
import com.api.streaming.application.useCases.MembershipConfig.DeleteMembershipConfigUseCase;
import com.api.streaming.application.useCases.MembershipConfig.GetAllMembershipConfigByPlatformUseCase;
import com.api.streaming.application.useCases.MembershipConfig.GetMembershipConfigUseCase;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;

@Configuration
public class MembershipConfigUseCasesConfig {
    @Bean
    CreateMembershipConfigUseCase createMembershipConfigUseCase (
        MembershipConfigInterface membershipConfigInterface,
        GenerateIdInterface generateIdInterface,
        PlatformInterface platformInterface
    ) {
        return new CreateMembershipConfigUseCase(membershipConfigInterface, generateIdInterface, platformInterface);
    }

    @Bean
    DeleteMembershipConfigUseCase deleteMembershipConfigUseCase(
        MembershipConfigInterface membershipConfigInterface,
        MembershipDetailsInterface membershipDetailsInterface
    ) {
        return new DeleteMembershipConfigUseCase(membershipConfigInterface, membershipDetailsInterface);
    }

    @Bean
    GetAllMembershipConfigByPlatformUseCase getAllMembershipConfigByPlatformUseCase(
        MembershipConfigInterface membershipConfigInterface
    ) {
        return new GetAllMembershipConfigByPlatformUseCase(membershipConfigInterface);
    }

    @Bean
    GetMembershipConfigUseCase getMembershipConfigUseCase(
        MembershipConfigInterface membershipConfigInterface
    ) {
        return new GetMembershipConfigUseCase(membershipConfigInterface);
    }
}
