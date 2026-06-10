package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.mappers;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.policy.domain.model.*;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.entities.PolicyEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

/**
 * Traductor encargado de mapear entre el modelo de dominio Policy
 * y la entidad de base de datos PolicyEntity.
 */
@Component
public class PolicyMapper {

    /**
     * Traduce del objeto rico de Dominio a la Entidad plana de Base de Datos.
     */
    public PolicyEntity toEntity(Policy domain) {
        if (domain == null) {
            return null;
        }

        return PolicyEntity.builder()
                .id(domain.getId().getValue())
                .policyNumber(domain.getPolicyNumber())
                .customerId(domain.getCustomerId().getValue())
                .branch(domain.getBranch().name())
                .ratingStrategy(domain.getRatingStrategy().name())
                .coverage(domain.getCoverage())
                .monthlyPremiumAmount(domain.getMonthlyPremium().getAmount())
                .monthlyPremiumCurrency(domain.getMonthlyPremium().getCurrency().getCurrencyCode())
                .riskProfile(domain.getRiskProfile())
                .status(domain.getStatus().name())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    /**
     * Traduce de la Entidad plana de Base de Datos al objeto rico de Dominio.
     */
    public Policy toDomain(PolicyEntity entity) {
        if (entity == null) {
            return null;
        }

        return Policy.builder()
                .id(new PolicyId(entity.getId()))
                .policyNumber(entity.getPolicyNumber())
                .customerId(new CustomerId(entity.getCustomerId()))
                .branch(Branch.valueOf(entity.getBranch()))
                .ratingStrategy(RatingStrategyType.valueOf(entity.getRatingStrategy()))
                .status(PolicyStatus.valueOf(entity.getStatus()))
                .coverage(entity.getCoverage())
                .monthlyPremium(new Money(entity.getMonthlyPremiumAmount(), Currency.getInstance(entity.getMonthlyPremiumCurrency())))
                .riskProfile(entity.getRiskProfile())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
