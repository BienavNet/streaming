package com.api.streaming.infrastructure.services;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.fasterxml.uuid.Generators;

@Component
public class GenerateIdService implements GenerateIdInterface{

    @Override
    public PlatformId generatePlatformId() {
        UUID uuid = Generators.timeBasedEpochGenerator().generate();
        return new PlatformId(uuid.toString());
    }

    @Override
    public AccountId generateAccountId() {
        UUID uuid = Generators.timeBasedEpochGenerator().generate();
        return new AccountId(uuid.toString());
    }

    @Override
    public PaymentId generatePaymentId() {
        UUID uuid = Generators.timeBasedEpochGenerator().generate();
        return new PaymentId(uuid.toString());
    }

    @Override
    public OrderId generateOrderId() {
        UUID uuid = Generators.timeBasedEpochGenerator().generate();
        return new OrderId(uuid.toString());
    }

    @Override
    public MembershipConfigId generatMembershipConfigId() {
        UUID uuid = Generators.timeBasedEpochGenerator().generate();
        return new MembershipConfigId(uuid.toString());
    }

    @Override
    public MembershipId generatMembershipId() {
        UUID uuid = Generators.timeBasedEpochGenerator().generate();
        return new MembershipId(uuid.toString());
    }
    
}
