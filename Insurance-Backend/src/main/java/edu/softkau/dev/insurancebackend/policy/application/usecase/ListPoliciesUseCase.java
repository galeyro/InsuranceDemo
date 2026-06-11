package edu.softkau.dev.insurancebackend.policy.application.usecase;

import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para listar todas las pólizas registradas.
 */
@Service
public class ListPoliciesUseCase {

    private final PolicyRepository repository;

    public ListPoliciesUseCase(PolicyRepository repository) {
        this.repository = repository;
    }

    public List<Policy> execute() {
        return repository.findAll();
    }
}
