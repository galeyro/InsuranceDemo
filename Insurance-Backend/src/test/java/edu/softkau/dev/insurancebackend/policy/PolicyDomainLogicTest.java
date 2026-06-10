package edu.softkau.dev.insurancebackend.policy;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.policy.application.factory.*;
import edu.softkau.dev.insurancebackend.policy.application.strategy.RatingStrategyRegistry;
import edu.softkau.dev.insurancebackend.policy.domain.exception.InvalidStateTransitionException;
import edu.softkau.dev.insurancebackend.policy.domain.exception.PolicyNotFoundException;
import edu.softkau.dev.insurancebackend.policy.domain.model.*;
import edu.softkau.dev.insurancebackend.policy.domain.states.*;
import edu.softkau.dev.insurancebackend.policy.domain.strategy.LoyaltyRatingStrategy;
import edu.softkau.dev.insurancebackend.policy.domain.strategy.RiskBasedRatingStrategy;
import edu.softkau.dev.insurancebackend.policy.domain.strategy.StandardRatingStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyDomainLogicTest {

    // ==========================================
    // 1. Money, Coverage, RiskProfile Tests
    // ==========================================

    @Test
    public void testMoneyOperations() {
        Money m1 = Money.cop(100.0);
        Money m2 = Money.cop(100.00);
        Money m3 = Money.usd(100.0);

        assertTrue(m1.isGreaterThanZero());
        assertFalse(Money.cop(0.0).isGreaterThanZero());
        assertFalse(Money.cop(-5.0).isGreaterThanZero());

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, "not-money");
        assertEquals(Objects.hash(m1.getAmount(), m1.getCurrency()), m1.hashCode());
        assertEquals("100.0 COP", m1.toString());

        assertThrows(NullPointerException.class, () -> new Money(null, Currency.getInstance("COP")));
        assertThrows(NullPointerException.class, () -> new Money(BigDecimal.ONE, null));
    }

    @Test
    public void testCoverageAndRiskProfile() {
        Money coverageAmt = Money.cop(100000);
        Map<String, Object> attrs = Map.of("copayRate", 0.2);
        Coverage cov1 = new Coverage(coverageAmt, 12, attrs);
        Coverage cov2 = new Coverage(coverageAmt, 12, attrs);

        assertEquals(cov1, cov2);
        assertEquals(coverageAmt, cov1.getCoverageAmount());
        assertEquals(12, cov1.getTermMonths());
        assertEquals(attrs, cov1.getAttributes());
        assertNotEquals(cov1, null);

        RiskProfile rp1 = new RiskProfile(30, 2022);
        RiskProfile rp2 = new RiskProfile(30, 2022);

        assertEquals(rp1, rp2);
        assertEquals(30, rp1.getRiskScore());
        assertEquals(2022, rp1.getCustomerSinceYear());
        assertNotEquals(rp1, null);
    }

    // ==========================================
    // 2. Policy and Policy.Builder Tests
    // ==========================================

    @Test
    public void testPolicyBuilderAndCreation() {
        PolicyId id = PolicyId.generate();
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Coverage coverage = new Coverage(Money.cop(80000000), 12, Map.of());
        Money premium = Money.cop(120000);
        RiskProfile riskProfile = new RiskProfile(10, 2021);

        Policy policy = Policy.builder()
                .id(id)
                .customerId(customerId)
                .branch(Branch.AUTO)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .coverage(coverage)
                .monthlyPremium(premium)
                .riskProfile(riskProfile)
                .build();

        assertNotNull(policy);
        assertEquals(id, policy.getId());
        assertNotNull(policy.getPolicyNumber());
        assertEquals(customerId, policy.getCustomerId());
        assertEquals(Branch.AUTO, policy.getBranch());
        assertEquals(RatingStrategyType.STANDARD, policy.getRatingStrategy());
        assertEquals(PolicyStatus.QUOTED, policy.getStatus());
        assertEquals(coverage, policy.getCoverage());
        assertEquals(premium, policy.getMonthlyPremium());
        assertEquals(riskProfile, policy.getRiskProfile());
        assertNotNull(policy.getCreatedAt());
        assertNotNull(policy.getUpdatedAt());
    }

    @Test
    public void testPolicyBuilderValidationExceptions() {
        PolicyId id = PolicyId.generate();
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Coverage coverage = new Coverage(Money.cop(80000000), 12, Map.of());
        Money premium = Money.cop(120000);
        RiskProfile riskProfile = new RiskProfile(10, 2021);

        assertThrows(NullPointerException.class, () -> Policy.builder().id(null).customerId(customerId).branch(Branch.AUTO).ratingStrategy(RatingStrategyType.STANDARD).coverage(coverage).monthlyPremium(premium).riskProfile(riskProfile).build());
        assertThrows(NullPointerException.class, () -> Policy.builder().id(id).customerId(null).branch(Branch.AUTO).ratingStrategy(RatingStrategyType.STANDARD).coverage(coverage).monthlyPremium(premium).riskProfile(riskProfile).build());
        assertThrows(NullPointerException.class, () -> Policy.builder().id(id).customerId(customerId).branch(null).ratingStrategy(RatingStrategyType.STANDARD).coverage(coverage).monthlyPremium(premium).riskProfile(riskProfile).build());
    }

    // ==========================================
    // 3. Factory Method Tests
    // ==========================================

    @Test
    public void testPolicyFactories() {
        AutoPolicyFactory autoFactory = new AutoPolicyFactory();
        HomePolicyFactory homeFactory = new HomePolicyFactory();
        LifePolicyFactory lifeFactory = new LifePolicyFactory();
        HealthPolicyFactory healthFactory = new HealthPolicyFactory();

        assertEquals(Branch.AUTO, autoFactory.getBranch());
        assertEquals(Branch.HOME, homeFactory.getBranch());
        assertEquals(Branch.LIFE, lifeFactory.getBranch());
        assertEquals(Branch.HEALTH, healthFactory.getBranch());

        assertNotNull(autoFactory.createDefaultCoverage());
        assertNotNull(homeFactory.createDefaultCoverage());
        assertNotNull(lifeFactory.createDefaultCoverage());
        assertNotNull(healthFactory.createDefaultCoverage());

        assertEquals(Money.cop(120000), autoFactory.getBasePremium());
        assertEquals(Money.cop(75000), homeFactory.getBasePremium());
        assertEquals(Money.cop(90000), lifeFactory.getBasePremium());
        assertEquals(Money.cop(180000), healthFactory.getBasePremium());

        PolicyFactoryRegistry registry = new PolicyFactoryRegistry(
                List.of(autoFactory, homeFactory, lifeFactory, healthFactory)
        );

        assertSame(autoFactory, registry.getFactory(Branch.AUTO));
        assertSame(homeFactory, registry.getFactory(Branch.HOME));
        assertSame(lifeFactory, registry.getFactory(Branch.LIFE));
        assertSame(healthFactory, registry.getFactory(Branch.HEALTH));

        assertThrows(IllegalArgumentException.class, () -> registry.getFactory(null));
    }

    // ==========================================
    // 4. Strategy Tests
    // ==========================================

    @Test
    public void testRatingStrategies() {
        StandardRatingStrategy standard = new StandardRatingStrategy();
        RiskBasedRatingStrategy riskBased = new RiskBasedRatingStrategy();
        LoyaltyRatingStrategy loyalty = new LoyaltyRatingStrategy();

        assertEquals(RatingStrategyType.STANDARD, standard.getName());
        assertEquals(RatingStrategyType.RISK_BASED, riskBased.getName());
        assertEquals(RatingStrategyType.LOYALTY, loyalty.getName());

        Money base = Money.cop(100000);
        RiskProfile rp = new RiskProfile(20, 2020);

        // Standard
        standard.validate(rp);
        assertEquals(base, standard.calculatePremium(base, rp));

        // Risk-based
        riskBased.validate(rp);
        // base * (1 + 20/100) = 120000
        assertEquals(Money.cop(120000), riskBased.calculatePremium(base, rp));
        assertThrows(IllegalArgumentException.class, () -> riskBased.validate(null));
        assertThrows(IllegalArgumentException.class, () -> riskBased.validate(new RiskProfile(null, 2020)));

        // Loyalty
        loyalty.validate(rp);
        // base * 0.85 = 85000
        assertEquals(Money.cop(85000), loyalty.calculatePremium(base, rp));
        assertThrows(NullPointerException.class, () -> loyalty.validate(null));
        assertThrows(NullPointerException.class, () -> loyalty.validate(new RiskProfile(20, null)));
        assertThrows(IllegalArgumentException.class, () -> loyalty.validate(new RiskProfile(20, java.time.LocalDate.now().getYear())));

        RatingStrategyRegistry registry = new RatingStrategyRegistry(
                List.of(standard, riskBased, loyalty)
        );
        assertSame(standard, registry.getStrategy(RatingStrategyType.STANDARD));
        assertSame(riskBased, registry.getStrategy(RatingStrategyType.RISK_BASED));
        assertSame(loyalty, registry.getStrategy(RatingStrategyType.LOYALTY));
        assertThrows(IllegalArgumentException.class, () -> registry.getStrategy(null));
    }

    // ==========================================
    // 5. State Machine / State Pattern Tests
    // ==========================================

    @Test
    public void testPolicyStateTransitions() {
        PolicyId id = PolicyId.generate();
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Coverage coverage = new Coverage(Money.cop(80000000), 12, Map.of());
        Money premium = Money.cop(120000);
        RiskProfile riskProfile = new RiskProfile(10, 2021);

        Policy policy = Policy.builder()
                .id(id)
                .customerId(customerId)
                .branch(Branch.AUTO)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .coverage(coverage)
                .monthlyPremium(premium)
                .riskProfile(riskProfile)
                .status(PolicyStatus.QUOTED)
                .build();

        // Initial State QUOTED
        assertEquals(PolicyStatus.QUOTED, policy.getStatus());

        // Idempotent transition
        policy.changeStatus(PolicyStatus.QUOTED);
        assertEquals(PolicyStatus.QUOTED, policy.getStatus());

        // Invalid: QUOTED -> ACTIVE
        assertThrows(InvalidStateTransitionException.class, () -> policy.changeStatus(PolicyStatus.ACTIVE));

        // Valid: QUOTED -> ISSUED
        policy.changeStatus(PolicyStatus.ISSUED);
        assertEquals(PolicyStatus.ISSUED, policy.getStatus());

        // Invalid: ISSUED -> SUSPENDED
        assertThrows(InvalidStateTransitionException.class, () -> policy.changeStatus(PolicyStatus.SUSPENDED));

        // Valid: ISSUED -> ACTIVE
        policy.changeStatus(PolicyStatus.ACTIVE);
        assertEquals(PolicyStatus.ACTIVE, policy.getStatus());

        // Valid: ACTIVE -> SUSPENDED
        policy.changeStatus(PolicyStatus.SUSPENDED);
        assertEquals(PolicyStatus.SUSPENDED, policy.getStatus());

        // Valid: SUSPENDED -> ACTIVE
        policy.changeStatus(PolicyStatus.ACTIVE);
        assertEquals(PolicyStatus.ACTIVE, policy.getStatus());

        // Valid: ACTIVE -> CANCELLED
        policy.changeStatus(PolicyStatus.CANCELLED);
        assertEquals(PolicyStatus.CANCELLED, policy.getStatus());

        // Invalid: CANCELLED -> ACTIVE (Terminal)
        assertThrows(InvalidStateTransitionException.class, () -> policy.changeStatus(PolicyStatus.ACTIVE));

        // Null target state validation
        assertThrows(IllegalArgumentException.class, () -> policy.changeStatus(null));
    }

    @Test
    public void testPolicyExceptions() {
        PolicyNotFoundException ex1 = new PolicyNotFoundException(PolicyId.generate());
        assertNotNull(ex1.getMessage());

        InvalidStateTransitionException ex2 = new InvalidStateTransitionException(PolicyStatus.QUOTED, PolicyStatus.ACTIVE);
        assertNotNull(ex2.getMessage());
    }
}
