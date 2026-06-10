package edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.persistence.repository;

import edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.persistence.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz de Spring Data JPA para realizar operaciones de base de datos directas.
 */
public interface JpaCustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
