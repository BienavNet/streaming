package com.api.streaming.infrastructure.services;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.valueObjects.AccountId;
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
    
}
