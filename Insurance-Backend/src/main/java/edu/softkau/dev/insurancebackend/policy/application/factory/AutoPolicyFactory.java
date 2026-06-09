package edu.softkau.dev.insurancebackend.policy.application.factory;

import edu.softkau.dev.insurancebackend.policy.domain.model.Branch;
import edu.softkau.dev.insurancebackend.policy.domain.model.Coverage;
import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyFactoryPort;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class AutoPolicyFactory implements PolicyFactoryPort {
    @Override
    public Branch getBranch() {
        return Branch.AUTO;
    }

    @Override
    public Coverage createDefaultCoverage() {
        Money coverageAmount  = new Money(BigDecimal.valueOf(80000000), Currency.getInstance("COP"));
        Money deductible = new Money(BigDecimal.valueOf(1000000), Currency.getInstance("COP"));
        return new Coverage(coverageAmount , 12, Map.of("deductible", deductible));
    }

    @Override
    public Money getBasePremium() {
        return new Money(BigDecimal.valueOf(120000), Currency.getInstance("COP"));
    }
}
