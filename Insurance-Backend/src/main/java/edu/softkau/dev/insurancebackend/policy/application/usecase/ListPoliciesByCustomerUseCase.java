package edu.softkau.dev.insurancebackend.policy.application.usecase;

import edu.softkau.dev.insurancebackend.customer.domain.exception.CustomerNotFoundException;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Caso de uso para obtener todas las pólizas asociadas a un cliente.
 */
@Service
public class ListPoliciesByCustomerUseCase {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;

    public ListPoliciesByCustomerUseCase(PolicyRepository policyRepository, CustomerRepository customerRepository) {
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Valida que el cliente exista y retorna la lista de pólizas asociadas.
     */
    public List<Policy> execute(UUID customerIdVal) {
        CustomerId customerId = new CustomerId(customerIdVal);

        if (customerRepository.findById(customerId).isEmpty()) {
            throw new CustomerNotFoundException(customerId);
        }

        return policyRepository.findByCustomerId(customerId);
    }
}
