package edu.softkau.dev.insurancebackend.customer.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Customer {

    private final CustomerId id;
    private String name;
    private Email email;
    private boolean isActive;
    private final Instant createdAt;
    private Instant updatedAt;

    // SCENARIO 1: Constructor Limpio para CREAR un cliente nuevo desde el negocio
    // ¡Solo pasas lo estrictamente necesario! El resto se calcula solo de forma segura.
    public Customer(CustomerId id, String name, Email email) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Objects.requireNonNull(email, "El email no puede ser nulo");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.isActive = true; // Negocio: Todo cliente nuevo nace activo
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // SCENARIO 2: Constructor gordo para RECONSTRUIR desde la persistencia (Infraestructura)
    // Lo ideal es dejarlo público solo para los mapeadores de infraestructura.
    public Customer(CustomerId id, String name, Email email, boolean isActive, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateName(String name) {
        Objects.requireNonNull(name, "El nombre no puede ser nulo");
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void changeEmail(Email email) {
        Objects.requireNonNull(email, "El email no puede ser nulo");
        this.email = email;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = Instant.now();
    }

    public CustomerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
