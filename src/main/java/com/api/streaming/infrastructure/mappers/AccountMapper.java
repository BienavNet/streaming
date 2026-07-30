package com.api.streaming.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.api.streaming.application.commands.CreateAccountCommand;
import com.api.streaming.application.commands.UpdateAccountCommand;
import com.api.streaming.application.queries.AccountDetailsQuery;
import com.api.streaming.domain.models.Account;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.Email;
import com.api.streaming.infrastructure.dtos.account.AccountDetailsResponse;
import com.api.streaming.infrastructure.dtos.account.AccountResponse;
import com.api.streaming.infrastructure.dtos.account.CreateAccountRequest;
import com.api.streaming.infrastructure.dtos.account.UpdateAccountRequest;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.entities.PaymentEntity;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;
import com.api.streaming.infrastructure.persistence.projections.AccountDetailsProjection;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PaymentMapper.class, PlatformMapper.class}
)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);


    Account fromEntityToDomain(AccountEntity entity);


    List<Account> fromEntityListToDomainList(List<AccountEntity> entities);

    @Mapping(
        target = "id",
        source = "account.id"
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

    AccountEntity fromDomainToEntity(Account domain);

    void updateAccountFromCommand(UpdateAccountCommand source, @MappingTarget Account target);
    
    CreateAccountCommand toCreateAccountCommandFromDto(CreateAccountRequest request);

    @Mapping(source = "platform.name", target = "platformName")
    AccountDetailsResponse toResponse(AccountDetailsQuery query);
    List<AccountDetailsResponse> fromQueryToResponse(List<AccountDetailsQuery> query);


    AccountResponse fromQueryToAccountResponse(AccountDetailsQuery query);


    @Mapping(
        target = "id",
        source = "accountId"
    )
    UpdateAccountCommand fromUpdateRequestToUpdateCommand(AccountId accountId, UpdateAccountRequest request);

    @Mapping(
        target = "platform", 
        expression = """
            java(
                new com.api.streaming.domain.models.Platform(
                    new com.api.streaming.domain.valueObjects.PlatformId(projection.getPlatformId()),
                    projection.getPlatformName(), 
                    projection.getPlatformLink(), 
                    projection.getPlatformIcon()
                    )
                )
        """
    ) 
    @Mapping(
        target = "payment", 
        expression = """
            java(
                new com.api.streaming.domain.models.Payment(
                    new com.api.streaming.domain.valueObjects.PaymentId(projection.getPaymentId()), 
                    projection.getPaymentNumber(), 
                    projection.getPaymentMonth(), 
                    projection.getPaymentYear(), 
                    projection.getPaymentCvv(), 
                    projection.getPaymentBankName()
                )
            )         
        """        
    )
    @Mapping(
        target = "membership", 
        expression = """
            java(
                projection.getMembershipId() == null ? null :
                new com.api.streaming.application.queries.MembershipDetailsQuery(
                    new com.api.streaming.domain.valueObjects.MembershipId(projection.getMembershipId()), 
                    new com.api.streaming.domain.models.MembershipConfig(
                        new com.api.streaming.domain.valueObjects.MembershipConfigId(projection.getMembershipConfigId()), 
                        new com.api.streaming.domain.valueObjects.PlatformId(projection.getPlatformId()), 
                        projection.getConfigPlan(), 
                        projection.getConfigProfiles()
                    ),
                    projection.getIsFreeTrial(),
                    projection.getMembershipBuyDate(),
                    projection.getMembershipExpireDate()
                )
            )
        """
    )
    AccountDetailsQuery fromProjectionToQuery(AccountDetailsProjection projection);

    List<AccountDetailsQuery> fromProjectionListToQueryList(List<AccountDetailsProjection> projection);

    default AccountId mapStringToAccountId(String id) {
        return id != null ? new AccountId(id) : null;
    }

    default String mapAccountIdToString(AccountId id) {
        return id == null ? null : id.value();
    }

    default Email mapStringToEmail(String email) {
        return email == null ? null : new Email(email);
    }

    default String mapEmailToString(Email email) {
        return email == null ? null : email.value();
    }

}
