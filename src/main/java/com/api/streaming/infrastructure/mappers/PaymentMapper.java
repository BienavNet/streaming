package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.commands.CreatePaymentCommand;
import com.api.streaming.domain.models.Payment;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.infrastructure.dtos.payment.CreatePaymentRequest;
import com.api.streaming.infrastructure.dtos.payment.PaymentResponse;
import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    default PaymentId mapStringToPaymentId(String id) {
        return id == null ? null : new PaymentId(id);
    }

    default String mapPaymentIdToString(PaymentId id) {
        return id == null ? null : id.value();
    }

    Payment fromEntityToDomain(PaymentEntity entity);
    PaymentEntity fromDomainToEntity(Payment domain);
    List<Payment> fromEntityListToDomainList(List<PaymentEntity> entities);


    CreatePaymentCommand createPaymentFromDtoToCommand(CreatePaymentRequest request);

    @Mapping(target = "card", expression = "java(payment.getNumber() + \"|\" + payment.getMonth() + \"|\" + payment.getYear() + \"|\" + payment.getCvv())")
    PaymentResponse fromDomainToPaymentResponse(Payment payment);
}