package edu.softkau.dev.insurancebackend.customer.domain.model;

import java.util.UUID;
import java.util.Objects;

public final class CustomerId {
    private final UUID value;

    public CustomerId(UUID value){
        this.value = Objects.requireNonNull(value, "El ID del cliente no puede ser nulo");
    }

    public static CustomerId generate(){
        return new CustomerId(UUID.randomUUID());
    }

    public static CustomerId fromString(String value){
        return new CustomerId(UUID.fromString(value));
    }

    public UUID getValue(){
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
