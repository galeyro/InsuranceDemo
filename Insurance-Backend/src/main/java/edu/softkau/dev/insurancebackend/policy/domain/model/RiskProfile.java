package edu.softkau.dev.insurancebackend.policy.domain.model;

import java.util.Objects;

public final class RiskProfile {
    private final Integer riskScore;
    private final Integer customerSinceYear;

    public RiskProfile(Integer riskScore, Integer customerSinceYear){
        if (riskScore != null && (riskScore < 0 || riskScore > 100)) {
            throw new IllegalArgumentException("El puntaje de riesgo debe estar entre 0 y 100");
        }

        if (customerSinceYear != null && customerSinceYear < 1900) {
            throw new IllegalArgumentException("El año de registro del cliente no es válido");
        }

        this.riskScore = riskScore;
        this.customerSinceYear = customerSinceYear;

    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public Integer getCustomerSinceYear() {
        return customerSinceYear;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskProfile that = (RiskProfile) o;
        return Objects.equals(riskScore, that.riskScore) && Objects.equals(customerSinceYear, that.customerSinceYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riskScore, customerSinceYear);
    }

    @Override
    public String toString() {
        return "RiskProfile{" +
                "riskScore=" + riskScore +
                ", customerSinceYear=" + customerSinceYear +
                '}';
    }
}
