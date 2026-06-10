package edu.softkau.dev.insurancebackend.customer.application.usecase;

import edu.softkau.dev.insurancebackend.customer.domain.exception.EmailAlreadyExistsException;
import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Caso de uso para registrar un nuevo cliente en el sistema.
 */
@Service
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Valida que el email sea único, crea el cliente y lo persiste.
     */
    public Customer execute(String name, String emailStr) {
        Email email = new Email(emailStr);

        if (customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        Customer customer = Customer.create(new CustomerId(UUID.randomUUID()), name, email);
        customerRepository.save(customer);

        return customer;
    }
}
