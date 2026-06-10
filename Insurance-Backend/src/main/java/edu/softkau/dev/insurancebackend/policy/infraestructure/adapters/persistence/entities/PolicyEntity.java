package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.entities;

import edu.softkau.dev.insurancebackend.policy.domain.model.Coverage;
import edu.softkau.dev.insurancebackend.policy.domain.model.RiskProfile;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.converters.CoverageConverter;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.converters.RiskProfileConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla 'policies' en PostgreSQL.
 * Almacena los datos planos de persistencia de la póliza.
 */
@Entity
@Table(name = "policies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "policy_number", unique = true, nullable = false)
    private String policyNumber;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "branch", nullable = false)
    private String branch;

    @Column(name = "rating_strategy", nullable = false)
    private String ratingStrategy;

    @Column(name = "coverage", columnDefinition = "text")
    @Convert(converter = CoverageConverter.class)
    private Coverage coverage;

    @Column(name = "monthly_premium_amount", nullable = false)
    private BigDecimal monthlyPremiumAmount;

    @Column(name = "monthly_premium_currency", nullable = false)
    private String monthlyPremiumCurrency;

    @Column(name = "risk_profile", columnDefinition = "text")
    @Convert(converter = RiskProfileConverter.class)
    private RiskProfile riskProfile;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
