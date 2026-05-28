package com.api.streaming.domain.interfaces;

import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

public interface GenerateIdInterface {
    PlatformId generatePlatformId();
    AccountId generateAccountId();
    PaymentId generatePaymentId();
}
