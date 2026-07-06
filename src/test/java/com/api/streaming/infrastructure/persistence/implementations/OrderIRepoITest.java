package com.api.streaming.infrastructure.persistence.implementations;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.api.streaming.application.queries.OrderDetailsQuery;
import com.api.streaming.domain.enums.OrderType;
import com.api.streaming.domain.models.Order;
import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;
import com.api.streaming.infrastructure.mappers.AccountMapperImpl;
import com.api.streaming.infrastructure.mappers.OrderMapperImpl;
import com.api.streaming.infrastructure.mappers.PaymentMapperImpl;
import com.api.streaming.infrastructure.mappers.PlatformMapperImpl;
import com.api.streaming.infrastructure.persistence.entities.AccountEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipConfigEntity;
import com.api.streaming.infrastructure.persistence.entities.MembershipEntity;
import com.api.streaming.infrastructure.persistence.entities.OrderEntity;
import com.api.streaming.infrastructure.persistence.entities.PlatformEntity;
import com.api.streaming.infrastructure.persistence.repositories.AccountJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipConfigJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.MembershipJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.OrderJpaRepository;
import com.api.streaming.infrastructure.persistence.repositories.PlatformJpaRepository;

@DataJpaTest
@Import(
    {
        OrderIRepository.class, 
        OrderMapperImpl.class, 
        AccountMapperImpl.class,
        PaymentMapperImpl.class,
        PlatformMapperImpl.class
    }
)
public class OrderIRepoITest {

    @Autowired
    private OrderIRepository orderIRepository;

    @Autowired
    private OrderJpaRepository jpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private PlatformJpaRepository platformJpaRepository;

    @Autowired
    private MembershipConfigJpaRepository membershipConfigJpaRepository;

    @Autowired
    private MembershipJpaRepository membershipJpaRepository;

    // Métodos Helper para creación de Entidades
    private OrderEntity createOrderEntity(String id, String accountId, String orderType) {
        return new OrderEntity(id, accountId, 2, LocalDate.now(), LocalDate.now().plusMonths(1), 20.50, orderType);
    }

    private AccountEntity createAccountEntity(String id, String platformId) {
        AccountEntity entity = new AccountEntity();
        entity.setId(id);
        entity.setEmail("test@domain.com");
        entity.setPlatform(platformId);
        return entity;
    }

    private PlatformEntity createPlatformEntity(String id) {
        PlatformEntity entity = new PlatformEntity();
        entity.setId(id);
        entity.setName("Netflix");
        entity.setIcon("netflix-icon.png");
        return entity;
    }

    private MembershipConfigEntity createMembershipConfigEntity(String id) {
        MembershipConfigEntity entity = new MembershipConfigEntity();
        entity.setId(id);
        entity.setPlatform("NETFLIX");
        entity.setPlan("PREMIUM");
        entity.setProfiles(4);
        return entity;
    }

    private MembershipEntity createMembershipEntity(String id, String accountId, String configId) {
        MembershipEntity entity = new MembershipEntity();
        entity.setId(id);
        entity.setAccount(accountId);
        entity.setMembershipConfig(configId);
        entity.setFreeTrial(false);
        entity.setMembershipBuyDate(LocalDate.now());
        entity.setMembershipExpireDate(LocalDate.now().plusMonths(1));
        return entity;
    }

    private void seedGraphDependencies(String accountId, String platformId, String configId, String membershipId) {
        PlatformEntity platform = this.createPlatformEntity(platformId);
        this.platformJpaRepository.saveAndFlush(platform);

        AccountEntity account = this.createAccountEntity(accountId, platformId);
        this.accountJpaRepository.saveAndFlush(account);

        MembershipConfigEntity config = this.createMembershipConfigEntity(configId);
        this.membershipConfigJpaRepository.saveAndFlush(config);

        MembershipEntity membership = this.createMembershipEntity(membershipId, accountId, configId);
        this.membershipJpaRepository.saveAndFlush(membership);
    }

    // --- Tests de operaciones CRUD básicas ---

    @Test
    void shouldFindOrderById() {
        OrderEntity entity = this.createOrderEntity("order-1", "acc-1", "PROFILE");
        this.jpaRepository.saveAndFlush(entity);

        Optional<Order> result = this.orderIRepository.getOrderById(new OrderId(entity.getId()));

        assertThat(result).isPresent();
        assertThat(result.get())
            .usingRecursiveComparison()
            .ignoringFields("id", "account", "orderType")
            .isEqualTo(entity);
    }

    @Test
    void shouldReturnAllOrders() {
        OrderEntity entity1 = this.createOrderEntity("order-1", "acc-1", "PROFILE");
        OrderEntity entity2 = this.createOrderEntity("order-2", "acc-2", "FULL");
        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2));

        List<Order> result = this.orderIRepository.getOrders();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindOrdersByAccountId() {
        OrderEntity entity1 = this.createOrderEntity("order-1", "acc-77", "PROFILE");
        OrderEntity entity2 = this.createOrderEntity("order-2", "acc-77", "FULL");
        OrderEntity entity3 = this.createOrderEntity("order-3", "acc-99", "PROFILE");
        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2, entity3));

        List<Order> result = this.orderIRepository.getOrdersByAccountId(new AccountId("acc-77"));

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindOrdersByAccountIdAndOrderType() {
        OrderEntity entity1 = this.createOrderEntity("order-1", "acc-55", OrderType.PROFILE.getValue());
        OrderEntity entity2 = this.createOrderEntity("order-2", "acc-55", OrderType.FULL.getValue());
        this.jpaRepository.saveAllAndFlush(List.of(entity1, entity2));

        // Asumiendo que OrderType es un Enum/Objeto con un método getValue()
        OrderType orderType = OrderType.PROFILE; 
        List<Order> result = this.orderIRepository.getOrdersByAccountIdAndOrderType(new AccountId("acc-55"), orderType);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId().value()).isEqualTo("order-1");
    }

    @Test
    void shouldSaveNewOrder() {
        Order orderToSave = new Order(new OrderId("order-100"), new AccountId("acc-100"), 2, LocalDate.now(), LocalDate.now().plusMonths(1), 23.00, OrderType.fromValue("profile"));

        Order savedOrder = this.orderIRepository.saveOrder(orderToSave);
        Optional<OrderEntity> savedInDB = this.jpaRepository.findById(orderToSave.getId().value());

        assertThat(savedInDB).isPresent();
        assertThat(savedOrder)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id", "account", "orderType")
            .isEqualTo(savedInDB.get());
    }

    @Test
    void shouldDeleteARegisteredOrderById() {
        OrderEntity entity = this.createOrderEntity("order-50", "acc-1", "PROFILE");
        this.jpaRepository.saveAndFlush(entity);

        this.orderIRepository.deleteOrder(new OrderId(entity.getId()));
        Optional<OrderEntity> deleted = this.jpaRepository.findById(entity.getId());

        assertThat(deleted).isEmpty();
    }

    // --- Tests de consultas complejas (Details Queries) ---

    @Test
    void shouldGetOrdersDetailsByAccountId() {
        this.seedGraphDependencies("acc-composite", "plt-1", "cfg-1", "mem-1");
        
        OrderEntity order1 = this.createOrderEntity("order-1", "acc-composite", "PROFILE");
        OrderEntity order2 = this.createOrderEntity("order-2", "acc-composite", "FULL");
        this.jpaRepository.saveAllAndFlush(List.of(order1, order2));

        List<OrderDetailsQuery> result = this.orderIRepository.getOrdersDetailsByAccountId(new AccountId("acc-composite"));

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldGetOrderDetailsById() {
        this.seedGraphDependencies("acc-unique", "plt-1", "cfg-1", "mem-1");
        
        OrderEntity order = this.createOrderEntity("order-999", "acc-unique", "PROFILE");
        this.jpaRepository.saveAndFlush(order);

        Optional<OrderDetailsQuery> result = this.orderIRepository.getOrderDetailsById(new OrderId("order-999"));

        assertThat(result).isPresent();
    }

    @Test
    void shouldGetOrdersDetails() {
        this.seedGraphDependencies("acc-all", "plt-1", "cfg-1", "mem-1");
        
        OrderEntity order = this.createOrderEntity("order-888", "acc-all", "PROFILE");
        this.jpaRepository.saveAndFlush(order);

        List<OrderDetailsQuery> result = this.orderIRepository.getOrdersDetails();

        assertThat(result).isNotEmpty();
    }
}
