package edu.softkau.dev.insurancebackend.policy.domain.strategy;

import edu.softkau.dev.insurancebackend.policy.application.factory.PolicyFactoryRegistry;
import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.model.RatingStrategyType;
import edu.softkau.dev.insurancebackend.policy.domain.model.RiskProfile;
import edu.softkau.dev.insurancebackend.policy.domain.ports.RatingStrategyPort;
import org.springframework.stereotype.Component;

@Component
public class StandardRatingStrategy implements RatingStrategyPort {
    @Override
    public RatingStrategyType getName() {
        return RatingStrategyType.STANDARD;
    }

    @Override
    public void validate(RiskProfile riskProfile) {
        //No requiere ninguna validación, la dejamos vacia
    }

    @Override
    public Money calculatePremium(Money basePremium, RiskProfile riskProfile) {
        // No aplica ningun ajuste, se devuelve el valor exacto de la prima base mensuale
        return basePremium;
    }
}
