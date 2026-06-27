package com.api.streaming.domain.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

public interface MembershipConfigInterface {
    Optional<MembershipConfig> getMembershipConfig(MembershipConfigId id);
    List<MembershipConfig> getMembershipConfigsByPlatform(PlatformId platform);
    MembershipConfig saveMembershipConfig(MembershipConfig config);
    void deleteMembershipConfig(MembershipConfigId id);
}
