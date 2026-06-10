package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transportar los datos del perfil de riesgo asociados al cliente.
 */
@Getter
@Setter
public class RiskProfileDto {

    @Min(value = 0, message = "El puntaje de riesgo mínimo es 0")
    @Max(value = 100, message = "El puntaje de riesgo máximo es 100")
    private Integer riskScore;

    @Min(value = 1900, message = "El año de registro del cliente debe ser a partir de 1900")
    private Integer customerSinceYear;
}
