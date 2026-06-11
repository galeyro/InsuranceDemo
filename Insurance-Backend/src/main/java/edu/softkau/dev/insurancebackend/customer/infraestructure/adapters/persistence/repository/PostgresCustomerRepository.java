package edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.persistence.repository;

import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.persistence.entities.CustomerEntity;
import edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.persistence.mappers.CustomerMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de infraestructura que implementa el puerto CustomerRepository del dominio.
 * Utiliza JpaCustomerRepository y CustomerMapper.
 */
@Repository
public class PostgresCustomerRepository implements CustomerRepository {

    private final JpaCustomerRepository jpaRepository;
    private final CustomerMapper mapper;

    // Inyección de dependencias por constructor
    public PostgresCustomerRepository(JpaCustomerRepository jpaRepository, CustomerMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Object customer) {
        // Hacemos un cast de Object a Customer (tipo de dominio)
        Customer domainCustomer = (Customer) customer;

        // Traducimos a Entidad y guardamos en PostgreSQL
        CustomerEntity entity = mapper.toEntity(domainCustomer);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        // Buscamos la entidad por su UUID y, si existe, la traducimos a objeto de dominio
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(Email email) {
        // Buscamos la entidad por su email (String) y la traducimos a objeto de dominio
        return jpaRepository.findByEmail(email.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        // Comprobamos la existencia del email usando la interfaz de Spring Data
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}