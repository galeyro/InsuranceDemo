package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.messaging.consumers;

import edu.softkau.dev.insurancebackend.policy.domain.events.PolicyStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada (Inbound Adapter).
 * Escucha de forma independiente el mismo tópico para registrar auditoría de todas las transiciones.
 */
@Component
public class PolicyAuditConsumer {

    private static final Logger log = LoggerFactory.getLogger(PolicyAuditConsumer.class);

    @KafkaListener(topics = "policy-status-changed", groupId = "insurance-audit-group")
    public void consume(PolicyStatusChangedEvent event) {
        log.info("📊 [Consumer de Auditoría] Registrando cambio de estado en la bitácora histórica...");
        log.info("   -> Transición: {} ===> {}", event.oldStatus(), event.newStatus());
        log.info("   -> Tipo de Evento: {}", event.eventType());
        log.info("   -> Póliza #: {}", event.policyNumber());
        log.info("   -> Cliente ID: {}", event.customerId());
        log.info("   -> Fecha: {}", event.timestamp());
        log.info("   -> [ACCIÓN] Guardando registro de auditoría en la base de datos de históricos...");
    }
}
