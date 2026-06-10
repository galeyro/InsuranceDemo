package edu.softkau.dev.insurancebackend.policy.domain.strategy;

import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.model.RatingStrategyType;
import edu.softkau.dev.insurancebackend.policy.domain.model.RiskProfile;
import edu.softkau.dev.insurancebackend.policy.domain.ports.RatingStrategyPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RiskBasedRatingStrategy implements RatingStrategyPort {
    @Override
    public RatingStrategyType getName() {
        return RatingStrategyType.RISK_BASED;
    }

    @Override
    public void validate(RiskProfile riskProfile) {
        if (riskProfile == null || riskProfile.getRiskScore() == null){
            throw new IllegalArgumentException("El RiskProfile no puede ser nulo");
        }
    }

    @Override
    public Money calculatePremium(Money basePremium, RiskProfile riskProfile) {
        BigDecimal finalAmount = basePremium
                .getAmount()
                .multiply(BigDecimal.valueOf(1.0 + (riskProfile.getRiskScore() / 100.0)));

        return new Money(finalAmount, basePremium.getCurrency());
    }
}
