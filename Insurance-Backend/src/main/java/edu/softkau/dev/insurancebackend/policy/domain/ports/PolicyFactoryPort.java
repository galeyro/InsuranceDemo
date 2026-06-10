package edu.softkau.dev.insurancebackend.policy.domain.ports;

import edu.softkau.dev.insurancebackend.policy.domain.model.Branch;
import edu.softkau.dev.insurancebackend.policy.domain.model.Coverage;
import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyId;

public interface PolicyFactoryPort {
     Branch getBranch();
     Coverage createDefaultCoverage();
     Money getBasePremium();

}
