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
public class LifePolicyFactory implements PolicyFactoryPort {
    @Override
    public Branch getBranch() {
        return Branch.LIFE;
    }

    @Override
    public Coverage createDefaultCoverage() {
        Money coverageAmount = new Money(BigDecimal.valueOf(200000000), Currency.getInstance("COP"));
        Boolean beneficiaryRequired = true;
        
        return new Coverage(coverageAmount, 12, Map.of("beneficiaryRequired", beneficiaryRequired));
    }

    @Override
    public Money getBasePremium() {
        return new Money(BigDecimal.valueOf(90000), Currency.getInstance("COP"));
    }
}
