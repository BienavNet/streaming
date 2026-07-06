package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.MembershipDetailsInterface;
import com.api.streaming.application.useCases.Membership.CreateMembershipUseCase;
import com.api.streaming.application.useCases.Membership.DeleteMembershipByAccountUseCase;
import com.api.streaming.application.useCases.Membership.GetAllMembershipsUseCase;
import com.api.streaming.application.useCases.Membership.GetMembershipByAccountIdUseCase;
import com.api.streaming.application.useCases.Membership.UpdateMembershipUseCase;
import com.api.streaming.domain.interfaces.AccountInterface;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.MembershipInterface;
import com.api.streaming.infrastructure.mappers.MembershipMapper;

@Configuration
public class MembershipUseCasesConfig {
    
    @Bean
    CreateMembershipUseCase createMembershipUseCase(
        MembershipInterface membershipInterface,
        GenerateIdInterface generateIdInterface,
        AccountInterface accountInterface,
        MembershipConfigInterface membershipConfigInterface
    ) {
        return new CreateMembershipUseCase(membershipInterface, generateIdInterface, accountInterface, membershipConfigInterface);
    }

    @Bean
    DeleteMembershipByAccountUseCase deleteMembershipByAccountUseCase(
        MembershipInterface membershipInterface
    ) {
        return new DeleteMembershipByAccountUseCase(membershipInterface);
    }

    @Bean 
    GetAllMembershipsUseCase getAllMembershipsUseCase(
        MembershipDetailsInterface membershipDetailsInterface
    ) {
        return new GetAllMembershipsUseCase(membershipDetailsInterface);
    }

    @Bean
    GetMembershipByAccountIdUseCase getMembershipByAccountIdUseCase(
        MembershipDetailsInterface membershipDetailsInterface
    ) {
        return new GetMembershipByAccountIdUseCase(membershipDetailsInterface);
    }

    @Bean
    UpdateMembershipUseCase updateMembershipUseCase(
        MembershipInterface membershipInterface,
        MembershipMapper mapper
    ){
        return new UpdateMembershipUseCase(membershipInterface, mapper);
    }
}
