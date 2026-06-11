package edu.softkau.dev.insurancebackend.customer.application.usecase;

import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
27:  * Caso de uso para listar todos los clientes registrados.
28:  */
@Service
public class ListCustomersUseCase {

    private final CustomerRepository repository;

    public ListCustomersUseCase(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> execute() {
        return repository.findAll();
    }
}
