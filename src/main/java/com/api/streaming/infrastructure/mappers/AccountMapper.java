package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.commands.CreateAccountCommand;
import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.domain.valueObjects.PaymentId;
import com.api.streaming.domain.valueObjects.PlatformId;
import com.api.streaming.infrastructure.dtos.account.AccountDetailsResponse;
import com.api.streaming.infrastructure.dtos.account.AccountResponse;
import com.api.streaming.infrastructure.dtos.account.CreateAccountRequest;
import com.api.streaming.infrastructure.dtos.account.UpdateAccountRequest;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PaymentMapper.class, PlatformMapper.class}
)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(
        target = "id",
        qualifiedByName = "stringToAccountId"
    )
    @Mapping(
        target = "email",
        qualifiedByName = "stringToEmail"
    )
    Account fromEntityToDomain(AccountEntity entity);


    List<Account> fromEntityListToDomainList(List<AccountEntity> entities);

    @Mapping(
        target = "id",
        source = "account.id",
        qualifiedByName = "stringToAccountId"
    )
    @Mapping(
        target = "email",
        source = "account.email",
        qualifiedByName = "stringToEmail"
    )
    @Mapping(
        target = "platform",
        source = "platform"
    )
    @Mapping(
        target = "payment",
        source = "payment"
    )
    AccountDetailsQuery fromEntityToQuery(AccountEntity account, PlatformEntity platform, PaymentEntity payment);



    @Mapping(
        target = "email",
        qualifiedByName = "emailToString"
    )
    AccountEntity fromDomainToEntity(Account domain);

    @Mapping(
        target = "email",
        qualifiedByName = "emailToString"
    )
    void updateAccountFromCommand(UpdateAccountCommand source, @MappingTarget Account target);
    
    @Mapping(
        target = "email",
        qualifiedByName = "stringToEmail"
    )
    @Mapping(
        target = "platform",
        qualifiedByName =  "stringToPlatformId"
    )
    @Mapping(
        target = "payment",
        qualifiedByName = "stringToPaymentId"
    )
    CreateAccountCommand toCreateAccountCommandFromDto(CreateAccountRequest request);

    @Mapping(source = "platform.name", target = "platformName")
    @Mapping(target = "email", qualifiedByName = "emailToString")
    AccountDetailsResponse toResponse(AccountDetailsQuery query);
    List<AccountDetailsResponse> fromQueryToResponse(List<AccountDetailsQuery> query);

    @Mapping(
        target = "email",
        qualifiedByName = "emailToString"
    )
    AccountResponse fromQueryToAccountResponse(AccountDetailsQuery query);

    @Mapping(
        target = "email",
        qualifiedByName = "stringToEmail"
    )
    @Mapping(
        target = "id",
        source = "accountId"
    )
    UpdateAccountCommand fromUpdateRequestToUpdateCommand(AccountId accountId, UpdateAccountRequest request);

    @Named(value = "stringToAccountId")
    default AccountId mapStringToAccountId(String id) {
        return id != null ? new AccountId(id) : null;
    }

    default String mapAccountIdToString(AccountId id) {
        return id == null ? null : id.value();
    }

    @Named("stringToEmail")
    default Email mapStringToEmail(String email) {
        return email == null ? null : new Email(email);
    }

    @Named("emailToString")
    default String mapEmailToString(Email email) {
        return email == null ? null : email.value();
    }

    @Named( value = "stringToPlatformId")
    default PlatformId mapStringToPlatformId(String id) {
        return new PlatformId(id);
    }

    @Named( value = "stringToPaymentId")
    default PaymentId mapStringToPaymentId(String payment) {
        return new PaymentId(payment);
    }
}
