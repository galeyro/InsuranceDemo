package edu.softkau.dev.insurancebackend.policy.application.factory;

import edu.softkau.dev.insurancebackend.policy.domain.model.Branch;
import edu.softkau.dev.insurancebackend.policy.domain.model.Coverage;
import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyFactoryPort;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class HealthPolicyFactory implements PolicyFactoryPort {
    @Override
    public Branch getBranch() {
        return Branch.HEALTH;
    }

    @Override
    public Coverage createDefaultCoverage() {
        Integer waitingPeriodDays = 30;
        Double copayRate = 0.20;

        Money coverageAmount  = new Money(BigDecimal.valueOf(100000000), Currency.getInstance("COP"));

        return new Coverage(coverageAmount , null, Map.of("copayRate", copayRate, "waitingPeriodDays", waitingPeriodDays));
    }

    @Override
    public Money getBasePremium() {
        return new Money(BigDecimal.valueOf(180000), Currency.getInstance("COP"));
    }
}
