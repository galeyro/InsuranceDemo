package edu.softkau.dev.insurancebackend.customer.application.usecase;

import edu.softkau.dev.insurancebackend.customer.domain.exception.CustomerNotFoundException;
import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Caso de uso para obtener la información de un cliente por su ID.
 */
@Service
public class GetCustomerUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Busca el cliente en el repositorio; si no existe, lanza CustomerNotFoundException.
     */
    public Customer execute(UUID id) {
        CustomerId customerId = new CustomerId(id);
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
