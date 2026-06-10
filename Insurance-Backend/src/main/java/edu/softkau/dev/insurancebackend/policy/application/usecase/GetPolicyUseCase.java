package edu.softkau.dev.insurancebackend.policy.application.usecase;

import edu.softkau.dev.insurancebackend.policy.domain.exception.PolicyNotFoundException;
import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyId;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Caso de uso para obtener los detalles de una póliza específica por su ID.
 */
@Service
public class GetPolicyUseCase {

    private final PolicyRepository policyRepository;

    public GetPolicyUseCase(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    /**
     * Busca la póliza en el repositorio; si no se encuentra, lanza PolicyNotFoundException.
     */
    public Policy execute(UUID id) {
        PolicyId policyId = new PolicyId(id);
        return policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));
    }
}
