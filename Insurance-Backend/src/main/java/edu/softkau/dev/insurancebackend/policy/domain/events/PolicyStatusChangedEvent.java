package edu.softkau.dev.insurancebackend.policy.domain.events;

import edu.softkau.dev.insurancebackend.policy.domain.model.Branch;
import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;
import java.time.Instant;

/**
 * Evento de dominio inmutable que se dispara cuando una póliza cambia de estado.
 */
public record PolicyStatusChangedEvent(
    String eventType,
    String policyId,
    String policyNumber,
    String customerId,
    Branch branch,
    PolicyStatus oldStatus,
    PolicyStatus newStatus,
    Instant timestamp
) {}

