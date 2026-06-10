package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.softkau.dev.insurancebackend.policy.domain.model.RiskProfile;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor JPA para transformar el perfil de riesgo (RiskProfile) a formato JSON String
 * al persistir en PostgreSQL.
 */
@Converter(autoApply = false)
public class RiskProfileConverter implements AttributeConverter<RiskProfile, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(RiskProfile riskProfile) {
        if (riskProfile == null) {
            return null;
        }

        try {
            RiskProfileJson json = new RiskProfileJson();
            json.riskScore = riskProfile.getRiskScore();
            json.customerSinceYear = riskProfile.getCustomerSinceYear();
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializando RiskProfile a JSON", e);
        }
    }

    @Override
    public RiskProfile convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        try {
            RiskProfileJson json = objectMapper.readValue(dbData, RiskProfileJson.class);
            return new RiskProfile(json.riskScore, json.customerSinceYear);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializando RiskProfile desde JSON", e);
        }
    }

    /**
     * Clase auxiliar interna (DTO) para mapear la persistencia sin contaminar el dominio.
     */
    private static class RiskProfileJson {
        public Integer riskScore;
        public Integer customerSinceYear;
    }
}
