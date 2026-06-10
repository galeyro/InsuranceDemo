package edu.softkau.dev.insurancebackend.policy.domain.strategy;

import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.model.RatingStrategyType;
import edu.softkau.dev.insurancebackend.policy.domain.model.RiskProfile;
import edu.softkau.dev.insurancebackend.policy.domain.ports.RatingStrategyPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LoyaltyRatingStrategy implements RatingStrategyPort {
    @Override
    public RatingStrategyType getName() {
        return RatingStrategyType.LOYALTY;
    }

    @Override
    public void validate(RiskProfile riskProfile) {
        // riskProfile.customerSince (año) obligatorio; antigüedad ≥ 2 años
        java.util.Objects.requireNonNull(riskProfile, "El perfil de riesgo es obligatorio");
        java.util.Objects.requireNonNull(riskProfile.getCustomerSinceYear(), "El año de registro es obligatorio");

        int currentYear = java.time.LocalDate.now().getYear();
        int seniority = currentYear - riskProfile.getCustomerSinceYear();

        if (seniority < 2){
            throw new IllegalArgumentException("El cliente debe tener al menos 2 años de antiguedad");
        }
    }

    @Override
    public Money calculatePremium(Money basePremium, RiskProfile riskProfile) {
        // prima base × 0.85 (15 % de descuento)
        BigDecimal finalAmount = basePremium
                .getAmount()
                .multiply(BigDecimal.valueOf(0.85));
        return new Money(finalAmount, basePremium.getCurrency());
    }
}
