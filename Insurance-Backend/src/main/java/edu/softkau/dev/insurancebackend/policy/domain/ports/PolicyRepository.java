package edu.softkau.dev.insurancebackend.policy.domain.ports;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Outbound Port) de dominio.
 * Define las operaciones que el dominio necesita realizar sobre las pólizas en base de datos.
 */
public interface PolicyRepository {
    void save(Policy policy);
    Optional<Policy> findById(PolicyId id);
    List<Policy> findByCustomerId(CustomerId customerId);
}
