package edu.softkau.dev.insurancebackend.policy.application.strategy;

import edu.softkau.dev.insurancebackend.policy.domain.model.RatingStrategyType;
import edu.softkau.dev.insurancebackend.policy.domain.ports.RatingStrategyPort;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RatingStrategyRegistry {
    private final Map<RatingStrategyType, RatingStrategyPort> strategiesMap;

    public RatingStrategyRegistry(List<RatingStrategyPort> strategiesMap) {
        this.strategiesMap = new EnumMap<>(RatingStrategyType.class);

        for (RatingStrategyPort strategy : strategiesMap) {
            this.strategiesMap.put(strategy.getName(), strategy);
        }
    }

    public RatingStrategyPort getStrategy(RatingStrategyType strategyType) {
        RatingStrategyPort strategy = strategiesMap.get(strategyType);
        if (strategy == null) {
            throw new IllegalArgumentException("No se ha encontrado una estrategia para el tipo " + strategyType);
        }
        return strategy;
    }
}
