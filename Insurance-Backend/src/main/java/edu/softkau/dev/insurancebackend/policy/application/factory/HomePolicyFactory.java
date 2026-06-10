package edu.softkau.dev.insurancebackend.policy.application.factory;

import edu.softkau.dev.insurancebackend.policy.domain.model.Branch;
import edu.softkau.dev.insurancebackend.policy.domain.model.Coverage;
import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyFactoryPort;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Component
public class HomePolicyFactory implements PolicyFactoryPort {
    @Override
    public Branch getBranch() {
        return Branch.HOME;
    }

    @Override
    public Coverage createDefaultCoverage() {
        Money coverageAmount = new Money(BigDecimal.valueOf(150000000), Currency.getInstance("COP"));
        Money deductibleAmount = new Money(BigDecimal.valueOf(2000000), Currency.getInstance("COP"));
        
        return new Coverage(coverageAmount, 12, Map.of("deductible", deductibleAmount));
    }

    @Override
    public Money getBasePremium() {
        return new Money(BigDecimal.valueOf(75000), Currency.getInstance("COP"));
    }
}
