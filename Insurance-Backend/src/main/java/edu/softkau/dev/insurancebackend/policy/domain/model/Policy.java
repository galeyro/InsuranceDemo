package edu.softkau.dev.insurancebackend.policy.domain.model;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;

import java.time.Instant;
import java.util.Objects;

public class Policy {

    private final PolicyId id;
    private final String policyNumber;
    private final CustomerId customerId;
    private final Branch branch;
    private final RatingStrategyType ratingStrategy;
    private final Coverage coverage;
    private final Money monthlyPremium;
    private final RiskProfile riskProfile;
    private final Instant createdAt;
    private PolicyStatus status;
    private Instant updatedAt;

    private Policy(PolicyId id,
                  String policyNumber,
                  CustomerId customerId,
                  Branch branch,
                  RatingStrategyType ratingStrategy,
                  PolicyStatus status,
                  Coverage coverage,
                  Money monthlyPremium,
                  RiskProfile riskProfile,
                  Instant createdAt,
                  Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser nulo");
        this.policyNumber = Objects.requireNonNull(policyNumber, "El número de póliza no puede ser nulo");
        if (policyNumber.isBlank()) {
            throw new IllegalArgumentException("El número de póliza no puede estar vacío");
        }
        this.customerId = Objects.requireNonNull(customerId, "El ID del cliente no puede ser nulo");
        this.branch = Objects.requireNonNull(branch, "El ramo no puede ser nulo");
        this.ratingStrategy = Objects.requireNonNull(ratingStrategy, "La estrategia de tarificación no puede ser nula");
        this.status = Objects.requireNonNull(status, "El estado no puede ser nulo");
        this.coverage = Objects.requireNonNull(coverage, "La cobertura no puede ser nula");
        this.monthlyPremium = Objects.requireNonNull(monthlyPremium, "La prima mensual no puede ser nula");
        this.riskProfile = Objects.requireNonNull(riskProfile, "El perfil de riesgo no puede ser nulo");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creación no puede ser nula");
        this.updatedAt = Objects.requireNonNull(updatedAt, "La fecha de actualización no puede ser nula");
    }

    public static Policy create(PolicyId id,
                                String policyNumber,
                  CustomerId customerId,
                                Branch branch,
                                RatingStrategyType ratingStrategy,
                                Coverage coverage,
                                Money monthlyPremium,
                                RiskProfile riskProfile) {
        Instant now = Instant.now();
        return new Policy(
                id,
                policyNumber,
                customerId,
                branch,
                ratingStrategy,
                PolicyStatus.QUOTED,
                coverage,
                monthlyPremium,
                riskProfile,
                now,
                now
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public void changeStatus(PolicyStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "El estado no puede ser nulo");
        this.updatedAt = Instant.now();
    }

    public PolicyId getId() {
        return id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Branch getBranch() {
        return branch;
    }

    public RatingStrategyType getRatingStrategy() {
        return ratingStrategy;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public Coverage getCoverage() {
        return coverage;
    }

    public Money getMonthlyPremium() {
        return monthlyPremium;
    }

    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static final class Builder {
        private PolicyId id;
        private String policyNumber;
        private CustomerId customerId;
        private Branch branch;
        private RatingStrategyType ratingStrategy;
        private PolicyStatus status;
        private Coverage coverage;
        private Money monthlyPremium;
        private RiskProfile riskProfile;
        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {
        }

        public Builder id(PolicyId id) {
            this.id = id;
            return this;
        }

        public Builder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        public Builder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder branch(Branch branch) {
            this.branch = branch;
            return this;
        }

        public Builder ratingStrategy(RatingStrategyType ratingStrategy) {
            this.ratingStrategy = ratingStrategy;
            return this;
        }

        public Builder status(PolicyStatus status) {
            this.status = status;
            return this;
        }

        public Builder coverage(Coverage coverage) {
            this.coverage = coverage;
            return this;
        }

        public Builder monthlyPremium(Money monthlyPremium) {
            this.monthlyPremium = monthlyPremium;
            return this;
        }

        public Builder riskProfile(RiskProfile riskProfile) {
            this.riskProfile = riskProfile;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Policy build() {
            if (this.status == null) {
                this.status = PolicyStatus.QUOTED;
            }
            if (this.createdAt == null) {
                this.createdAt = java.time.Instant.now();
            }
            if (this.updatedAt == null) {
                this.updatedAt = java.time.Instant.now();
            }

            java.util.Objects.requireNonNull(id, "El ID es obligatorio");
            java.util.Objects.requireNonNull(policyNumber, "El número de póliza es obligatorio");
            java.util.Objects.requireNonNull(customerId, "El ID de cliente es obligatorio");
            java.util.Objects.requireNonNull(branch, "El ramo es obligatorio");
            java.util.Objects.requireNonNull(ratingStrategy, "La estrategia de tarificación es obligatoria");
            java.util.Objects.requireNonNull(coverage, "La cobertura es obligatoria");
            java.util.Objects.requireNonNull(monthlyPremium, "La prima mensual es obligatoria");
            java.util.Objects.requireNonNull(riskProfile, "El perfil de riesgo es obligatorio");

            return new Policy(id, policyNumber, customerId, branch, ratingStrategy,
                    status, coverage, monthlyPremium, riskProfile,
                    createdAt, updatedAt);
        }
    }
}
