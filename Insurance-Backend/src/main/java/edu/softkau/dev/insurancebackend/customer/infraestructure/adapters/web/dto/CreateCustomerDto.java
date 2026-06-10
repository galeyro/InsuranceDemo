package edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de transferencia de datos (DTO) para la creación de un cliente.
 * Contiene anotaciones de validación declarativa de Bean Validation.
 */
@Getter
@Setter
public class CreateCustomerDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico es inválido")
    private String email;
}
