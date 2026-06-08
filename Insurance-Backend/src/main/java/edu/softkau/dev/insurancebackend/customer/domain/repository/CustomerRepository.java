package edu.softkau.dev.insurancebackend.customer.domain.repository;

import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;

import java.util.Optional;

public interface CustomerRepository {
    void save(Object customer);
    Optional<Customer> findById(CustomerId id);
    Optional<Customer> findByEmail(Email email);
    boolean existsByEmail(Email email);
}
