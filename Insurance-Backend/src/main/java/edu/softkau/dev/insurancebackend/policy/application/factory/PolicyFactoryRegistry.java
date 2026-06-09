package edu.softkau.dev.insurancebackend.policy.application.factory;

import edu.softkau.dev.insurancebackend.policy.domain.model.Branch;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyFactoryPort;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PolicyFactoryRegistry {
    private final Map<Branch, PolicyFactoryPort> factoriesMap;

    public PolicyFactoryRegistry(List<PolicyFactoryPort> factories) {
        this.factoriesMap = new EnumMap<>(Branch.class);

        for (PolicyFactoryPort factory : factories) {
            this.factoriesMap.put(factory.getBranch(), factory);
        }
    }

    public PolicyFactoryPort getFactory(Branch branch) {
        PolicyFactoryPort factory = factoriesMap.get(branch);

        if (factory == null) {
            throw new IllegalArgumentException("No se ha encontrado un factoria para la rama " + branch);
        }

        return factory;
    }
}
