package com.api.streaming.application.useCases.MembershipConfig;

import com.api.streaming.application.commands.CreateMembershipConfigCommand;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.MembershipConfigInterface;
import com.api.streaming.domain.interfaces.PlatformInterface;
import com.api.streaming.domain.models.MembershipConfig;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateMembershipConfigUseCase {
    private final MembershipConfigInterface membershipConfigInterface;
    private final GenerateIdInterface generateIdInterface;

    // external services required
    private final PlatformInterface platformInterface;

    public MembershipConfigId execute(CreateMembershipConfigCommand command) {
        this.checkIfPlatformExists(command.platform());

        MembershipConfigId id = this.generateIdInterface.generateMembershipConfigId();
        MembershipConfig membershipConfig = new MembershipConfig(id, command.platform(), command.plan(), command.profiles());

        this.membershipConfigInterface.saveMembershipConfig(membershipConfig);

        return id;
    }

    private void checkIfPlatformExists(PlatformId platformId){
        this.platformInterface.getPlatform(platformId)
            .orElseThrow(() -> new EntityNotFoundException("This platform does not exists."));
    }
}
