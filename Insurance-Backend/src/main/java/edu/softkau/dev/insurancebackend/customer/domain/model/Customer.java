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

    public Customer(CustomerId id, String name, Email email,
                    boolean isActive, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser nulo");
        this.email = Objects.requireNonNull(email, "El email no puede ser nulo");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.name = name;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Customer create(CustomerId id, String name, Email email) {
        Instant now = Instant.now();
        return new Customer(id, name, email, true, now, now);
    }

    public static Builder builder() {
        return new Builder();
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

    public static final class Builder {
        private CustomerId id;
        private String name;
        private Email email;
        private boolean isActive;
        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {
        }

        public Builder id(CustomerId id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Customer build() {
            return new Customer(id, name, email, isActive, createdAt, updatedAt);
        }
    }
}
