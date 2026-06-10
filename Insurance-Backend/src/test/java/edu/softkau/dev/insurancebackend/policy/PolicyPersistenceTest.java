package edu.softkau.dev.insurancebackend.policy;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.policy.domain.model.*;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.converters.CoverageConverter;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.converters.RiskProfileConverter;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.entities.PolicyEntity;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.mappers.PolicyMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyPersistenceTest {

    private final CoverageConverter coverageConverter = new CoverageConverter();
    private final RiskProfileConverter riskProfileConverter = new RiskProfileConverter();
    private final PolicyMapper policyMapper = new PolicyMapper();

    @Test
    public void testCoverageConverter() {
        // Arrange
        Money amount = new Money(BigDecimal.valueOf(80000000), Currency.getInstance("COP"));
        Coverage coverage = new Coverage(amount, 12, Map.of("deductible", 1000000.0, "gpsRequired", true));

        // Act
        String dbData = coverageConverter.convertToDatabaseColumn(coverage);
        assertNotNull(dbData);
        assertTrue(dbData.contains("80000000"));
        assertTrue(dbData.contains("COP"));
        assertTrue(dbData.contains("deductible"));

        Coverage restored = coverageConverter.convertToEntityAttribute(dbData);

        // Assert
        assertNotNull(restored);
        assertEquals(coverage.getCoverageAmount(), restored.getCoverageAmount());
        assertEquals(coverage.getTermMonths(), restored.getTermMonths());
        assertEquals(coverage.getAttributes().get("deductible"), restored.getAttributes().get("deductible"));
        assertEquals(coverage.getAttributes().get("gpsRequired"), restored.getAttributes().get("gpsRequired"));
    }

    @Test
    public void testRiskProfileConverter() {
        // Arrange
        RiskProfile riskProfile = new RiskProfile(75, 2024);

        // Act
        String dbData = riskProfileConverter.convertToDatabaseColumn(riskProfile);
        assertNotNull(dbData);
        assertTrue(dbData.contains("75"));
        assertTrue(dbData.contains("2024"));

        RiskProfile restored = riskProfileConverter.convertToEntityAttribute(dbData);

        // Assert
        assertNotNull(restored);
        assertEquals(riskProfile.getRiskScore(), restored.getRiskScore());
        assertEquals(riskProfile.getCustomerSinceYear(), restored.getCustomerSinceYear());
    }

    @Test
    public void testPolicyMapperBidirectional() {
        // Arrange
        PolicyId id = new PolicyId(UUID.randomUUID());
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Money amount = new Money(BigDecimal.valueOf(80000000), Currency.getInstance("COP"));
        Coverage coverage = new Coverage(amount, 12, Map.of("deductible", 1000000.0));
        Money premium = new Money(BigDecimal.valueOf(120000), Currency.getInstance("COP"));
        RiskProfile riskProfile = new RiskProfile(15, 2022);

        Policy domainPolicy = Policy.builder()
                .id(id)
                .policyNumber("POL-2026-000001")
                .customerId(customerId)
                .branch(Branch.AUTO)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .status(PolicyStatus.QUOTED)
                .coverage(coverage)
                .monthlyPremium(premium)
                .riskProfile(riskProfile)
                .build();

        // Act
        PolicyEntity entity = policyMapper.toEntity(domainPolicy);

        assertNotNull(entity);
        assertEquals(id.getValue(), entity.getId());
        assertEquals("POL-2026-000001", entity.getPolicyNumber());
        assertEquals(customerId.getValue(), entity.getCustomerId());
        assertEquals("AUTO", entity.getBranch());
        assertEquals("STANDARD", entity.getRatingStrategy());
        assertEquals(coverage, entity.getCoverage());
        assertEquals(BigDecimal.valueOf(120000), entity.getMonthlyPremiumAmount());
        assertEquals("COP", entity.getMonthlyPremiumCurrency());
        assertEquals(riskProfile, entity.getRiskProfile());
        assertEquals("QUOTED", entity.getStatus());

        Policy restoredDomain = policyMapper.toDomain(entity);

        // Assert
        assertNotNull(restoredDomain);
        assertEquals(domainPolicy.getId(), restoredDomain.getId());
        assertEquals(domainPolicy.getPolicyNumber(), restoredDomain.getPolicyNumber());
        assertEquals(domainPolicy.getCustomerId(), restoredDomain.getCustomerId());
        assertEquals(domainPolicy.getBranch(), restoredDomain.getBranch());
        assertEquals(domainPolicy.getRatingStrategy(), restoredDomain.getRatingStrategy());
        assertEquals(domainPolicy.getStatus(), restoredDomain.getStatus());
        assertEquals(domainPolicy.getCoverage(), restoredDomain.getCoverage());
        assertEquals(domainPolicy.getMonthlyPremium(), restoredDomain.getMonthlyPremium());
        assertEquals(domainPolicy.getRiskProfile(), restoredDomain.getRiskProfile());
    }
}
