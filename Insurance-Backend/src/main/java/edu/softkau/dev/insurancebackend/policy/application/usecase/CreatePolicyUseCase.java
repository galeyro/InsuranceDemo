package edu.softkau.dev.insurancebackend.policy.application.usecase;

import edu.softkau.dev.insurancebackend.customer.domain.exception.CustomerNotFoundException;
import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import edu.softkau.dev.insurancebackend.policy.application.factory.PolicyFactoryRegistry;
import edu.softkau.dev.insurancebackend.policy.application.strategy.RatingStrategyRegistry;
import edu.softkau.dev.insurancebackend.policy.domain.model.*;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyFactoryPort;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyRepository;
import edu.softkau.dev.insurancebackend.policy.domain.ports.RatingStrategyPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Caso de uso para cotizar y registrar una nueva póliza de seguros.
 * Integra los patrones: Factory Method (cobertura/prima base), Strategy (ajuste de prima) y Builder (ensamblado).
 */
@Service
public class CreatePolicyUseCase {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final PolicyFactoryRegistry factoryRegistry;
    private final RatingStrategyRegistry strategyRegistry;

    public CreatePolicyUseCase(PolicyRepository policyRepository,
                               CustomerRepository customerRepository,
                               PolicyFactoryRegistry factoryRegistry,
                               RatingStrategyRegistry strategyRegistry) {
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
        this.factoryRegistry = factoryRegistry;
        this.strategyRegistry = strategyRegistry;
    }

    /**
     * Ejecuta el flujo de cotización y creación.
     */
    public Policy execute(UUID customerIdVal, Branch branch, RatingStrategyType strategyType, RiskProfile riskProfile) {
        // 1. Validar que el cliente exista y esté activo
        CustomerId customerId = new CustomerId(customerIdVal);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        if (!customer.isActive()) {
            throw new IllegalArgumentException("El cliente no está activo");
        }

        // 2. Obtener cobertura por defecto y prima base (Factory Method)
        PolicyFactoryPort factory = factoryRegistry.getFactory(branch);
        Coverage defaultCoverage = factory.createDefaultCoverage();
        Money basePremium = factory.getBasePremium();

        // 3. Calcular la prima final ajustada (Strategy)
        RatingStrategyPort strategy = strategyRegistry.getStrategy(strategyType);
        strategy.validate(riskProfile);
        Money monthlyPremium = strategy.calculatePremium(basePremium, riskProfile);

        // 4. Construir la póliza (Builder)
        Policy policy = Policy.builder()
                .id(new PolicyId(UUID.randomUUID()))
                .customerId(customerId)
                .branch(branch)
                .ratingStrategy(strategyType)
                .coverage(defaultCoverage)
                .monthlyPremium(monthlyPremium)
                .riskProfile(riskProfile)
                .status(PolicyStatus.QUOTED)
                .build();

        // 5. Guardar en base de datos
        policyRepository.save(policy);

        return policy;
    }
}
