package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.repository;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyId;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyRepository;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.entities.PolicyEntity;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.mappers.PolicyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de infraestructura que implementa la interfaz PolicyRepository del dominio.
 * Utiliza JpaPolicyRepository y PolicyMapper para interactuar con PostgreSQL.
 */
@Repository
public class PostgresPolicyRepository implements PolicyRepository {

    private final JpaPolicyRepository jpaRepository;
    private final PolicyMapper mapper;

    public PostgresPolicyRepository(JpaPolicyRepository jpaRepository, PolicyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Policy policy) {
        if (policy == null) {
            return;
        }
        PolicyEntity entity = mapper.toEntity(policy);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Policy> findById(PolicyId id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Policy> findByCustomerId(CustomerId customerId) {
        if (customerId == null) {
            return List.of();
        }
        List<PolicyEntity> entities = jpaRepository.findByCustomerId(customerId.getValue());
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Policy> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
