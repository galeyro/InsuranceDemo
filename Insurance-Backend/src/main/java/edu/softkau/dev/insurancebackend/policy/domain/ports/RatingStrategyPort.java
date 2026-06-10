package edu.softkau.dev.insurancebackend.policy.domain.ports;

import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.model.RatingStrategyType;
import edu.softkau.dev.insurancebackend.policy.domain.model.RiskProfile;

public interface RatingStrategyPort {
    RatingStrategyType getName();
    void validate(RiskProfile riskProfile);
    Money calculatePremium(Money basePremium, RiskProfile riskProfile);
}
