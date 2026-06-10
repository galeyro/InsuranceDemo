package edu.softkau.dev.insurancebackend.policy;

import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import edu.softkau.dev.insurancebackend.policy.application.factory.*;
import edu.softkau.dev.insurancebackend.policy.application.strategy.RatingStrategyRegistry;
import edu.softkau.dev.insurancebackend.policy.application.usecase.*;
import edu.softkau.dev.insurancebackend.policy.domain.events.PolicyStatusChangedEvent;
import edu.softkau.dev.insurancebackend.policy.domain.exception.PolicyNotFoundException;
import edu.softkau.dev.insurancebackend.policy.domain.model.*;
import edu.softkau.dev.insurancebackend.policy.domain.ports.*;
import edu.softkau.dev.insurancebackend.policy.domain.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PolicyUseCaseTest {

    private PolicyRepository policyRepository;
    private CustomerRepository customerRepository;
    private PolicyEventPublisherPort eventPublisher;
    
    private CreatePolicyUseCase createPolicyUseCase;
    private GetPolicyUseCase getPolicyUseCase;
    private ListPoliciesByCustomerUseCase listPoliciesByCustomerUseCase;
    private ChangePolicyStatusUseCase changePolicyStatusUseCase;

    @BeforeEach
    public void setUp() {
        policyRepository = mock(PolicyRepository.class);
        customerRepository = mock(CustomerRepository.class);
        eventPublisher = mock(PolicyEventPublisherPort.class);

        // Instanciamos registros reales para integrar la lógica de Factory y Strategy en la prueba
        PolicyFactoryRegistry factoryRegistry = new PolicyFactoryRegistry(
                List.of(new AutoPolicyFactory(), new HomePolicyFactory(), new LifePolicyFactory(), new HealthPolicyFactory())
        );
        RatingStrategyRegistry strategyRegistry = new RatingStrategyRegistry(
                List.of(new StandardRatingStrategy(), new RiskBasedRatingStrategy(), new LoyaltyRatingStrategy())
        );

        createPolicyUseCase = new CreatePolicyUseCase(policyRepository, customerRepository, factoryRegistry, strategyRegistry);
        getPolicyUseCase = new GetPolicyUseCase(policyRepository);
        listPoliciesByCustomerUseCase = new ListPoliciesByCustomerUseCase(policyRepository, customerRepository);
        changePolicyStatusUseCase = new ChangePolicyStatusUseCase(eventPublisher);
    }

    @Test
    public void testCreatePolicySuccess() {
        UUID customerUuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(customerUuid);
        Customer customer = Customer.create(customerId, "John Doe", new Email("john@example.com"));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        RiskProfile riskProfile = new RiskProfile(10, 2021);
        Policy result = createPolicyUseCase.execute(customerUuid, Branch.AUTO, RatingStrategyType.STANDARD, riskProfile);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(Branch.AUTO, result.getBranch());
        assertEquals(RatingStrategyType.STANDARD, result.getRatingStrategy());
        assertEquals(PolicyStatus.QUOTED, result.getStatus());
        assertEquals(Money.cop(120000), result.getMonthlyPremium()); // Auto base premium is 120000

        verify(policyRepository, times(1)).save(result);
    }

    @Test
    public void testCreatePolicyThrowsIfCustomerInactive() {
        UUID customerUuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(customerUuid);
        Customer customer = Customer.create(customerId, "John Doe", new Email("john@example.com"));
        customer.deactivate();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        RiskProfile riskProfile = new RiskProfile(10, 2021);

        assertThrows(IllegalArgumentException.class, () -> {
            createPolicyUseCase.execute(customerUuid, Branch.AUTO, RatingStrategyType.STANDARD, riskProfile);
        });

        verify(policyRepository, never()).save(any(Policy.class));
    }

    @Test
    public void testGetPolicySuccess() {
        UUID policyUuid = UUID.randomUUID();
        PolicyId policyId = new PolicyId(policyUuid);
        Policy policy = Policy.builder()
                .id(policyId)
                .customerId(new CustomerId(UUID.randomUUID()))
                .branch(Branch.HOME)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .coverage(new Coverage(Money.cop(75000000), 12, Map.of()))
                .monthlyPremium(Money.cop(75000))
                .riskProfile(new RiskProfile(0, 2020))
                .build();

        when(policyRepository.findById(policyId)).thenReturn(Optional.of(policy));

        Policy result = getPolicyUseCase.execute(policyUuid);

        assertNotNull(result);
        assertEquals(policyId, result.getId());
    }

    @Test
    public void testGetPolicyThrowsIfNotFound() {
        UUID policyUuid = UUID.randomUUID();
        PolicyId policyId = new PolicyId(policyUuid);

        when(policyRepository.findById(policyId)).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () -> {
            getPolicyUseCase.execute(policyUuid);
        });
    }

    @Test
    public void testListPoliciesByCustomer() {
        UUID customerUuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(customerUuid);
        List<Policy> policies = List.of(
                Policy.builder()
                        .id(PolicyId.generate())
                        .customerId(customerId)
                        .branch(Branch.HOME)
                        .ratingStrategy(RatingStrategyType.STANDARD)
                        .coverage(new Coverage(Money.cop(75000000), 12, Map.of()))
                        .monthlyPremium(Money.cop(75000))
                        .riskProfile(new RiskProfile(0, 2020))
                        .build()
        );

        Customer customer = Customer.create(customerId, "John Doe", new Email("john@example.com"));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(policyRepository.findByCustomerId(customerId)).thenReturn(policies);

        List<Policy> result = listPoliciesByCustomerUseCase.execute(customerUuid);

        assertEquals(1, result.size());
        assertEquals(customerId, result.get(0).getCustomerId());
    }

    @Test
    public void testChangePolicyStatusAndPublishEvent() {
        Policy policy = Policy.builder()
                .id(PolicyId.generate())
                .customerId(new CustomerId(UUID.randomUUID()))
                .branch(Branch.AUTO)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .coverage(new Coverage(Money.cop(80000000), 12, Map.of()))
                .monthlyPremium(Money.cop(120000))
                .riskProfile(new RiskProfile(5, 2022))
                .status(PolicyStatus.QUOTED)
                .build();

        // QUOTED -> ISSUED (valid transition)
        changePolicyStatusUseCase.execute(policy, PolicyStatus.ISSUED);

        assertEquals(PolicyStatus.ISSUED, policy.getStatus());

        ArgumentCaptor<PolicyStatusChangedEvent> eventCaptor = ArgumentCaptor.forClass(PolicyStatusChangedEvent.class);
        verify(eventPublisher, times(1)).publish(eventCaptor.capture());

        PolicyStatusChangedEvent event = eventCaptor.getValue();
        assertEquals("policy.issued", event.eventType());
        assertEquals(policy.getId().toString(), event.policyId());
        assertEquals(PolicyStatus.QUOTED, event.oldStatus());
        assertEquals(PolicyStatus.ISSUED, event.newStatus());

        // Cancelled transitions
        Policy policy2 = Policy.builder()
                .id(PolicyId.generate())
                .customerId(new CustomerId(UUID.randomUUID()))
                .branch(Branch.AUTO)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .coverage(new Coverage(Money.cop(80000000), 12, Map.of()))
                .monthlyPremium(Money.cop(120000))
                .riskProfile(new RiskProfile(5, 2022))
                .status(PolicyStatus.QUOTED)
                .build();
        changePolicyStatusUseCase.execute(policy2, PolicyStatus.CANCELLED);
        assertEquals(PolicyStatus.CANCELLED, policy2.getStatus());

        // Activated transitions
        Policy policy3 = Policy.builder()
                .id(PolicyId.generate())
                .customerId(new CustomerId(UUID.randomUUID()))
                .branch(Branch.AUTO)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .coverage(new Coverage(Money.cop(80000000), 12, Map.of()))
                .monthlyPremium(Money.cop(120000))
                .riskProfile(new RiskProfile(5, 2022))
                .status(PolicyStatus.ISSUED)
                .build();
        changePolicyStatusUseCase.execute(policy3, PolicyStatus.ACTIVE);
        assertEquals(PolicyStatus.ACTIVE, policy3.getStatus());

        // Suspended transitions
        changePolicyStatusUseCase.execute(policy3, PolicyStatus.SUSPENDED);
        assertEquals(PolicyStatus.SUSPENDED, policy3.getStatus());

        // Reactivated transitions
        changePolicyStatusUseCase.execute(policy3, PolicyStatus.ACTIVE);
        assertEquals(PolicyStatus.ACTIVE, policy3.getStatus());

        // No transition (idempotent targetStatus == currentStatus) -> event should not be published again
        changePolicyStatusUseCase.execute(policy3, PolicyStatus.ACTIVE);
        verify(eventPublisher, times(5)).publish(any(PolicyStatusChangedEvent.class)); // 1 (policy1) + 1 (policy2) + 3 (policy3 transitions) = 5
    }
}
