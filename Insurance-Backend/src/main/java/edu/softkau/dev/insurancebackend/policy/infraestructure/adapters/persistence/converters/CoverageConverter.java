package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.softkau.dev.insurancebackend.policy.domain.model.Coverage;
import edu.softkau.dev.insurancebackend.policy.domain.model.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

/**
 * Convertidor JPA para transformar la cobertura (Coverage) a formato JSON String
 * al persistir en PostgreSQL.
 */
@Converter(autoApply = false)
public class CoverageConverter implements AttributeConverter<Coverage, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Coverage coverage) {
        if (coverage == null) {
            return null;
        }

        try {
            CoverageJson json = new CoverageJson();
            json.coverageAmount = coverage.getCoverageAmount().getAmount();
            json.currency = coverage.getCoverageAmount().getCurrency().getCurrencyCode();
            json.termMonths = coverage.getTermMonths();
            json.attributes = coverage.getAttributes();
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializando Coverage a JSON", e);
        }
    }

    @Override
    public Coverage convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        try {
            CoverageJson json = objectMapper.readValue(dbData, CoverageJson.class);
            Money money = new Money(json.coverageAmount, Currency.getInstance(json.currency));
            return new Coverage(money, json.termMonths, json.attributes);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializando Coverage desde JSON", e);
        }
    }

    /**
     * Clase auxiliar interna (DTO) para mapear la persistencia sin contaminar el dominio.
     */
    private static class CoverageJson {
        public BigDecimal coverageAmount;
        public String currency;
        public Integer termMonths;
        public Map<String, Object> attributes;
    }
}
