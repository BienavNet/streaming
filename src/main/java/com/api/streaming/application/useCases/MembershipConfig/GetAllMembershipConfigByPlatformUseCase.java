package com.api.streaming.application.useCases.MembershipConfig;

import java.util.List;

import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.PlatformId;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAllMembershipConfigByPlatformUseCase {
    private final MembershipConfigInterface membershipConfigInterface;

    public List<MembershipConfig> execute(PlatformId platform) {
        return this.membershipConfigInterface.getMembershipConfigsByPlatform(platform);
    }
}
