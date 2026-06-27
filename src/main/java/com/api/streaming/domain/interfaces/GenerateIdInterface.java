package com.api.streaming.domain.interfaces;

import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.MembershipConfigId;
import com.api.streaming.domain.valueObjects.MembershipId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;

public interface GenerateIdInterface {
    PlatformId generatePlatformId();
    AccountId generateAccountId();
    PaymentId generatePaymentId();
    OrderId generateOrderId();
    MembershipConfigId generatMembershipConfigId();
    MembershipId generatMembershipId();
}
