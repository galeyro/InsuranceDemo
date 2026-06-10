package edu.softkau.dev.insurancebackend.policy;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.policy.application.usecase.ChangePolicyStatusUseCase;
import edu.softkau.dev.insurancebackend.policy.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class PolicyStatusChangedIntegrationTest {

    @Autowired
    private ChangePolicyStatusUseCase changePolicyStatusUseCase;

    @Test
    public void testPolicyStatusTransitionAndEventPublishing() throws InterruptedException {
        // 1. Creamos una póliza en estado inicial QUOTED usando el Builder
        Policy policy = Policy.builder()
                .id(PolicyId.generate())
                .policyNumber("POL-TEST-001")
                .customerId(new CustomerId(UUID.randomUUID()))
                .branch(Branch.AUTO)
                .ratingStrategy(RatingStrategyType.STANDARD)
                .coverage(new Coverage(Money.cop(100000.0), 12, java.util.Map.of()))
                .monthlyPremium(Money.cop(150.0))
                .riskProfile(new RiskProfile(5, 2020))
                .status(PolicyStatus.QUOTED)
                .build();

        System.out.println("\n=== [TEST] INICIANDO PRUEBA DE TRANSICIÓN DE ESTADO ===");
        System.out.println("[TEST] Póliza creada. Estado inicial: " + policy.getStatus());

        // 2. Ejecutamos el caso de uso para cambiar el estado a ISSUED (Emitido)
        System.out.println("[TEST] Ejecutando caso de uso para transicionar a ISSUED...");
        changePolicyStatusUseCase.execute(policy, PolicyStatus.ISSUED);

        // 3. Esperamos 3 segundos para dar tiempo a que el hilo de Kafka procese el mensaje
        // y nuestro "PolicyNotificationConsumer" lo lea.
        System.out.println("[TEST] Esperando que el consumidor de Kafka procese el evento...");
        TimeUnit.SECONDS.sleep(3);

        System.out.println("=== [TEST] FIN DE LA PRUEBA ===\n");
    }
}