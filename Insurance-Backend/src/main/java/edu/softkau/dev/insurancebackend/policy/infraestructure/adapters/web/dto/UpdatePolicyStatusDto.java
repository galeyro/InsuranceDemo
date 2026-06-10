package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.web.dto;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la petición de cambio de estado de una póliza.
 */
@Getter
@Setter
public class UpdatePolicyStatusDto {

    @NotNull(message = "El estado objetivo (targetStatus) es obligatorio")
    private PolicyStatus targetStatus;
}
