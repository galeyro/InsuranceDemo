package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.web.dto;

import edu.softkau.dev.insurancebackend.policy.domain.model.Branch;
import edu.softkau.dev.insurancebackend.policy.domain.model.RatingStrategyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO para la petición de cotización y creación de una póliza.
 */
@Getter
@Setter
public class CreatePolicyDto {

    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID customerId;

    @NotNull(message = "El ramo (branch) es obligatorio")
    private Branch branch;

    @NotNull(message = "La estrategia de tarificación (ratingStrategy) es obligatoria")
    private RatingStrategyType ratingStrategy;

    @NotNull(message = "El perfil de riesgo es obligatorio")
    @Valid
    private RiskProfileDto riskProfile;
}
