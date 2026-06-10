package edu.softkau.dev.insurancebackend.policy.domain.model;

import java.util.Map;
import java.util.Objects;

public final class Coverage {
    private final Money coverageAmount;
    private final Integer termMonths;
    private final Map<String, Object> attributes;

    public Coverage(Money coverageAmount, Integer termMonths, Map<String, Object> attributes){
        Objects.requireNonNull(coverageAmount, "El monto de la cobertura no puede ser nulo");

        if (!coverageAmount.isGreaterThanZero()) {
            throw new IllegalArgumentException("El monto de la cobertura debe ser mayor a 0");
        }

        if (termMonths != null && termMonths <= 0) {
            throw new IllegalArgumentException("El plazo en meses debe ser mayor a 0");
        }

        Objects.requireNonNull(attributes, "Los atributos de la cobertura no pueden ser nulos");

        this.coverageAmount = coverageAmount;
        this.termMonths = termMonths;

        //
        this.attributes = Map.copyOf(attributes);
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coverage coverage = (Coverage) o;
        return coverageAmount.equals(coverage.coverageAmount) && Objects.equals(termMonths, coverage.termMonths) && attributes.equals(coverage.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coverageAmount, termMonths, attributes);
    }

    @Override
    public String toString() {
        return "Coverage{" +
                "coverageAmount=" + coverageAmount +
                ", termMonths=" + termMonths +
                ", attributes=" + attributes +
                '}';
    }

    public Money getCoverageAmount() {
        return coverageAmount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
