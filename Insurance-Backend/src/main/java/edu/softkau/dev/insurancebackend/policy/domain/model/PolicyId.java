package edu.softkau.dev.insurancebackend.policy.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class PolicyId {
    private final UUID value;

    public PolicyId(UUID value) {
        this.value = Objects.requireNonNull(value, "El ID de la póliza no puede ser nulo");
    }

    public static PolicyId generate() {
        return new PolicyId(UUID.randomUUID());
    }

    public static PolicyId fromString(String value) {
        return new PolicyId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyId that = (PolicyId) o;
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
