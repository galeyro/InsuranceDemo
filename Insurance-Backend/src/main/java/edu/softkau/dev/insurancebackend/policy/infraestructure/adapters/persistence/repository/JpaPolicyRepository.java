package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.repository;

import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.entities.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaPolicyRepository extends JpaRepository<PolicyEntity, UUID> {
    
    /**
     * Busca todas las entidades de pólizas asociadas a un cliente específico.
     */
    List<PolicyEntity> findByCustomerId(UUID customerId);

    /**
     * Obtiene el número de póliza máximo registrado que coincide con un patrón específico (ej. POL-2026-%).
     */
    @Query("SELECT MAX(p.policyNumber) FROM PolicyEntity p WHERE p.policyNumber LIKE :pattern")
    String findMaxPolicyNumber(@Param("pattern") String pattern);
}
