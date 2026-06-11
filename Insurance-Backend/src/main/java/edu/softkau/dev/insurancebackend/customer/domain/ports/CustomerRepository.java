package edu.softkau.dev.insurancebackend.customer.domain.ports;

import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void save(Object customer);
    Optional<Customer> findById(CustomerId id);
    Optional<Customer> findByEmail(Email email);
    boolean existsByEmail(Email email);
    List<Customer> findAll();
}
