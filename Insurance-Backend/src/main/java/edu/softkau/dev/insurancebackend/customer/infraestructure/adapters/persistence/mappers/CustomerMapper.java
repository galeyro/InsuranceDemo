package edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.persistence.mappers;

import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;
import edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.persistence.entities.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    /**
     * Traduce del objeto rico de Dominio a la Entidad plana de Base de Datos.
     */
    public CustomerEntity toEntity(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerEntity.builder()
                .id(customer.getId().getValue())       // Extrae el UUID del Value Object CustomerId
                .name(customer.getName())
                .email(customer.getEmail().getValue()) // Extrae el String del Value Object Email
                .isActive(customer.isActive())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    /**
     * Traduce de la Entidad plana de Base de Datos al objeto rico de Dominio.
     */
    public Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }

        return Customer.builder()
                .id(new CustomerId(entity.getId()))       // Reconstruye el Value Object CustomerId
                .name(entity.getName())
                .email(new Email(entity.getEmail()))     // Reconstruye el Value Object Email
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}