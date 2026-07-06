package com.api.streaming.domain.models;

import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.PlatformId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipConfig {
    private MembershipConfigId id;
    private PlatformId platform;
    private String plan;
    private Integer profiles;


    public MembershipConfig(MembershipConfigId id, PlatformId platform, String plan, Integer profiles) {
        this.id = id;
        this.platform = platform;
        this.plan = plan;
        this.profiles = profiles;

        this.validate();
    }

    public void setProfiles(Integer amount) {
        if (amount <=0)
            throw new IllegalArgumentException("Profiles amount should be more than 0.");
        this.profiles = amount;
    }   

    private void validate() {
        // Profiles cannot be 0 or less than that 
        if (this.profiles <=0 )
            throw new IllegalArgumentException("Profiles amount should be more than 0.");
    }


}
